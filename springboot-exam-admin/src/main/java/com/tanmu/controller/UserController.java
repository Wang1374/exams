package com.tanmu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanmu.common.Result;
import com.tanmu.entity.User;
import com.tanmu.service.UserService;
import com.tanmu.util.GetCodeUtil;
import com.tanmu.util.HMACSHA256;
import com.tanmu.util.RsaUtil;
import com.tanmu.util.TokenUtil;
import com.tanmu.vo.RegisterUser;
import com.tanmu.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

@RestController //返回json格式
@RequestMapping("/user") //user接口
public class UserController {
    @Autowired
    private UserService userService;
    @Value("${resetPassword}")
    private String resetPassword;
    @Value("${privateKey}")
    private String privateKey;
    //添加用户
    @PostMapping("/save")  //?表示任意一种类型
    public Result<?> save(@RequestBody User user){
        //注册之前先判断手机号是否已经注册
        boolean byPhone = userService.checkUserByPhone(user.getPhone());
        if(byPhone){
            //存在 不允许再次注册
            return Result.error("500","手机号已存在");
        }else{
            //允许注册
            Result result = userService.saveUser(user);
            return result;
        }
    }

    //分页模糊查询所有用户
   @GetMapping("/getUserAll")
    public Result<?> getUserAll(@RequestParam(defaultValue ="1") Integer pageNumber,@RequestParam(defaultValue = "10") Integer pageSize,@RequestParam(defaultValue = "") String search){
       //分页查询
       Page<User> page = userService.getUserAll(pageNumber,pageSize,search);
       return Result.success(page);
   }

    //更新数据
    @PutMapping("/updateUser")
    public Result<?> updateUser(@RequestBody User user){
        int i = userService.updateUser(user);
        if(i==1){
            return Result.success();
        }else{
            return Result.error("500","修改失败");
        }
    }

    //删除用户
    @DeleteMapping("/deleteUser/{id}")
    public Result<?> deleteUser(@PathVariable Integer id){
        int i = userService.deleteUser(id);
        if(i==1){
            return Result.success();
        }else{
            return Result.error("500","删除失败");
        }
    }

    //前台注册用户
    @PostMapping("/register")  //?表示任意一种类型
    public Result<?> register(@RequestBody RegisterUser registerUser, HttpServletRequest request){
        //解密
        String code= RsaUtil.decrypt(registerUser.getCode(), privateKey);
        String name= RsaUtil.decrypt(registerUser.getName(), privateKey);
        String nickName= RsaUtil.decrypt(registerUser.getNickName(), privateKey);
        String age= RsaUtil.decrypt(registerUser.getAge(), privateKey);
        String address= RsaUtil.decrypt(registerUser.getAddress(), privateKey);
        String sex= RsaUtil.decrypt(registerUser.getSex(), privateKey);
        String phone= RsaUtil.decrypt(registerUser.getPhone(), privateKey);
        String password= RsaUtil.decrypt(registerUser.getPassword(), privateKey);
        //解密后的user数据
        User user = new User();
        user.setName(name);
        user.setNickName(nickName);
        user.setAge(Integer.parseInt(age));
        user.setAddress(address);
        user.setSex(Integer.parseInt(sex));
        user.setPhone(phone);
        user.setPassword(password);
        //判断验证码是否正确
        //手机号|验证码
        String dxCode = (String) request.getSession().getAttribute("dxCode");
        String[] codes = dxCode.split(",");

        //手机号不是同一个
        if (!codes[0].equals(phone)){
            return Result.error("1003","手机号错误,不是同一个！");
        }

        //验证码错误
        if(!codes[1].equals(code)){
            return Result.error("1002","验证码错误！");
        }
        //注册之前先判断手机号是否已经注册
        boolean byPhone = userService.checkUserByPhone(phone);
        if(byPhone){
            //存在 不允许再次注册
            return Result.error("1001","手机号已存在！");
        }else {
            userService.saveUser(user);
            return Result.success();
        }
    }
    //根据手机号查询用户信息
    @PostMapping("/getUserByPhone")
    public Result<?> getUserByPhone(@RequestBody String phone){
        //解密
        phone = RsaUtil.decrypt(phone, privateKey);
        User user = userService.getUserByPhone(phone);
        if(user==null){
            return Result.error("500","用户信息不存在！");
        }else{
            return Result.success(user);
        }
    }
    //修改用户状态 是否禁用
    @PostMapping("/updateUserYn/{id}/{yn}")
    public Result<?> updateUserYn(@PathVariable Integer id,@PathVariable Integer yn){
        Result result = userService.updateUserYn(id, yn);
        return result;
    }
    //更新数据
    @PutMapping("/updatePass")
    public Result<?> updatePass(@RequestBody UserVo userVo, HttpServletRequest request){
        String token = request.getHeader("token");
        //判断是否同一个手机号
        if(!TokenUtil.isPhone(token,userVo.getPhone())){
            return Result.error("1003","手机号错误,不是同一个！");
        }
        //查询旧密码和旧密码的uuid
        User user = userService.getUserByPhone( userVo.getPhone());
        if(user==null){
            return Result.error("1004","手机号不存在！");
        }
        //判断旧密码是否正确
        String password = HMACSHA256.sha256_HMAC(userVo.getOldPassWord(), user.getKeyUuid());
        if (!user.getPassword().equals(password)){
            return Result.error("1005","旧密码错误！");
        }

        //判断新旧密码是否一致
        if(userVo.getOldPassWord().equals(userVo.getNewPassWord())){
            return Result.error("1006","新密码与旧密码一样,请重新更改！");
        }

        //旧密码相等 加密新密码
        String key = UUID.randomUUID().toString();
        String newPassword = HMACSHA256.sha256_HMAC(userVo.getNewPassWord(), key);
        user.setPassword(newPassword);
        user.setKeyUuid(key);
        //修改数据
        int i = userService.updateUser(user);
        if(i==1){
            return Result.success();
        }else{
            return Result.error("500","修改失败");
        }
    }
    //重置密码
    @PutMapping("/resetPassword")
    public Result<?> resetPassword(@RequestBody String phone, HttpServletRequest request) throws UnsupportedEncodingException {
        //解密
        phone = RsaUtil.decrypt(phone, privateKey);
        //判断权限是否是超级管理员
        String token = request.getHeader("token");
        String userPhone = TokenUtil.getUserPhone(token);
        User admin = userService.getUserByPhone(userPhone);
        if(admin==null){
            return Result.error("1007","token不存在！");
        }
        Integer role = admin.getRole();
        if(role!=0){
            return Result.error("1008","无权限重置密码,请联系管理员！");
        }

        //进行重置密码为ks123..
        User user = userService.getUserByPhone(phone);
        if(user==null){
            return Result.error("1004","手机号不存在！");
        }

        //修改数据
        String key = UUID.randomUUID().toString();
        user.setKeyUuid(key);
        user.setPassword(HMACSHA256.sha256_HMAC(resetPassword,key));
        int i = userService.updateUser(user);
        if(i==1){
            return Result.success();
        }else{
            return Result.error("500","重置密码失败");
        }
    }
    //用户重置密码
    @PostMapping("/userResetPassword")
    public Result<?> userResetPassword(@RequestParam("phone") String phone,@RequestParam("code") String code, HttpServletRequest request) throws Exception {
        //解密
        phone = RsaUtil.decrypt(phone, privateKey);
        code  = RsaUtil.decrypt(code, privateKey);
        HttpSession session = request.getSession();
        String sessionPhone="";
        String sessionCode="";
        String str=(String) session.getAttribute("forgetPasswordCode");

        if(str==null){
            return  Result.error("500","用户session过期！");
        }else{
            sessionPhone = str.split(",")[0];
            sessionCode  = str.split(",")[1];
        }

        //判断手机号是否同一个
        if(!phone.equals(sessionPhone)){
            return  Result.error("500","手机号不是同一个！");
        }

        //判断短信验证码code是否正确
        if(!code.equals(sessionCode)){
            return  Result.error("500","短信验证码错误！");
        }

        //进行重置密码
        User user = userService.getUserByPhone(phone);
        if(user==null){
            return Result.error("1004","手机号不存在！");
        }

        //修改数据
        String key = UUID.randomUUID().toString();
        user.setKeyUuid(key);
        user.setPassword(HMACSHA256.sha256_HMAC(resetPassword,key));
        int i = userService.updateUser(user);
        if(i==1){
            return Result.success();
        }else{
            return Result.error("500","重置密码失败");
        }
    }
}
