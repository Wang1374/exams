package com.tanmu.controller;

import com.tanmu.common.Result;
import com.tanmu.entity.User;
import com.tanmu.service.MarkTestPaperService;
import com.tanmu.service.UserService;
import com.tanmu.service.impl.MarkTestPaperServiceImpl;
import com.tanmu.util.RsaUtil;
import com.tanmu.util.TokenUtil;
import com.tanmu.vo.ExamVo;
import com.tanmu.vo.SubmitExamVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@RestController //返回json格式
@RequestMapping("/markTestPaper")
//批改试卷
public class MarkTestPaperController {
    @Autowired
    private MarkTestPaperService markTestPaperService;
    @Autowired
    private UserService userService;
    @Value("${privateKey}")
    private String privateKey;

    //获取试卷信息
  @GetMapping("/getUserExamMark")
    public Result<?> getUserExamMark(String id){
      //解密
      id=RsaUtil.decrypt(id,privateKey);
      Result userExamById = markTestPaperService.getUserExamById(Integer.parseInt(id));
      return userExamById;
  }

    @GetMapping("/adminGetUserExamMark")
    public Result<?> adminGetUserExamMark(String id){
        //解密
        id=RsaUtil.decrypt(id,privateKey);
        Result userExamById = markTestPaperService.getUserExamById(Integer.parseInt(id));
        return userExamById;
    }
    //提交试卷
    @PostMapping("/submitExam")
    public Result<?> submitExam(@RequestBody SubmitExamVo submitExamVo, HttpServletRequest request) throws UnsupportedEncodingException {
        String token = request.getHeader("token");
        String userPhone = TokenUtil.getUserPhone(token);
        User user = userService.getUserByPhone(userPhone);
        if(user==null){
            return Result.error("1007","token不存在！");
        }

        Result result = markTestPaperService.saveExam(submitExamVo);
        return result;
    }
}
