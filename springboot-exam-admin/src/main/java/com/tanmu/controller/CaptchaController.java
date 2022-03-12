
package com.tanmu.controller;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.anji.captcha.util.StringUtils;
import com.tanmu.common.Result;
import com.tanmu.entity.User;
import com.tanmu.service.UserService;
import com.tanmu.util.GetCodeUtil;
import com.tanmu.util.RsaUtil;
import com.tanmu.util.ShortMsgUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Random;


@RestController
@RequestMapping("/captcha")
public class CaptchaController {

    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private UserService userService;
    @Value("${privateKey}")
    private String privateKey;

    @PostMapping("/get")
    public ResponseModel get(@RequestBody CaptchaVO data, HttpServletRequest request) {
        assert request.getRemoteHost()!=null;
        data.setBrowserInfo(getRemoteId(request));
        return captchaService.get(data);
    }

    @PostMapping("/check")
    public ResponseModel check(@RequestBody CaptchaVO data, HttpServletRequest request) {
        data.setBrowserInfo(getRemoteId(request));
        return captchaService.check(data);
    }

    //发送短语验证码接口
    @PostMapping("/sendMsg")
    public Result sendMsg(@RequestParam("captchaVerification") String captchaVerification,@RequestParam("phone")String phone,HttpServletRequest request) throws Exception {
        //解密数据
        String phone1=RsaUtil.decrypt(phone, privateKey);
        phone=phone1;
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(captchaVerification);
        ResponseModel response = captchaService.verification(captchaVO);
        System.out.println("response = " + response.getRepCode());
        if(response.isSuccess() == false){
            System.out.println("---------->失败");
            //验证码校验失败，返回信息告诉前端
            //repCode  0000  无异常，代表成功
            //repCode  9999  服务器内部异常
            //repCode  0011  参数不能为空
            //repCode  6110  验证码已失效，请重新获取
            //repCode  6111  验证失败
            //repCode  6112  获取验证码失败,请联系管理员
            return Result.error(response.getRepCode(),response.getRepMsg());
        }
        System.out.println("---------->成功");
        //将验证码存储到session
        HttpSession session = request.getSession();
        String code = GetCodeUtil.getCode(6);//随机6位验证码
        session.setAttribute("dxCode",phone+","+code);

        System.out.println("手机号:"+phone+" 短信验证码:"+code+" 时间:"+new Date().toLocaleString());
        //发送短信
        ShortMsgUtil.SendMessage(phone,code);

        return Result.success("您的短信验证码为:"+code);
    }
    //找回密码发送短语验证码接口
    @PostMapping("/forgetPasswordSendMsg")
    public Result forgetPasswordSendMsg(@RequestParam("captchaVerification") String captchaVerification,@RequestParam("phone")String phone,@RequestParam("nickName")String nickName,HttpServletRequest request) throws Exception {
        //解密数据
        phone = RsaUtil.decrypt(phone, privateKey);
        nickName = RsaUtil.decrypt(nickName, privateKey);

        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(captchaVerification);
        ResponseModel response = captchaService.verification(captchaVO);
        System.out.println("response = " + response.getRepCode());
        if(response.isSuccess() == false){
            System.out.println("---------->失败");
            //验证码校验失败，返回信息告诉前端
            //repCode  0000  无异常，代表成功
            //repCode  9999  服务器内部异常
            //repCode  0011  参数不能为空
            //repCode  6110  验证码已失效，请重新获取
            //repCode  6111  验证失败
            //repCode  6112  获取验证码失败,请联系管理员
            return Result.error(response.getRepCode(),response.getRepMsg());
        }
        System.out.println("---------->滑动验证成功");
        //再次验证手机号 姓名是否存在
        User user = userService.getNickNameAndPhone(nickName, phone);
        if(user!=null){
            //将验证码存储到session
            HttpSession session = request.getSession();
            String code = GetCodeUtil.getCode(6);//随机6位验证码
            session.setAttribute("forgetPasswordCode",phone+","+code);
            //发送短信
            String s = ShortMsgUtil.forgetPasswordSendMessage(phone, code);
            System.out.println("找回密码发送短信返回信息:"+s);
            if(s.contains("OK")){
                return Result.success();
            }else{
                return Result.error("500","短信验证码发送失败,请稍后再试！");
            }
        }else{
            return Result.error("500","手机号或者真实姓名错误！");
        }
    }
    //@PostMapping("/verify")
    public ResponseModel verify(@RequestBody CaptchaVO data, HttpServletRequest request) {
        return captchaService.verification(data);
    }

    public static final String getRemoteId(HttpServletRequest request) {
        String xfwd = request.getHeader("X-Forwarded-For");
        String ip = getRemoteIpFromXfwd(xfwd);
        String ua = request.getHeader("user-agent");
        if (StringUtils.isNotBlank(ip)) {
            return ip + ua;
        }
        return request.getRemoteAddr() + ua;
    }

    private static String getRemoteIpFromXfwd(String xfwd) {
        if (StringUtils.isNotBlank(xfwd)) {
            String[] ipList = xfwd.split(",");
            return StringUtils.trim(ipList[0]);
        }
        return null;
    }

}
