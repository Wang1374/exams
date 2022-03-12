package com.tanmu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanmu.common.Result;
import com.tanmu.entity.Subject;
import com.tanmu.entity.User;
import com.tanmu.mapper.SubjectMapper;
import com.tanmu.service.ExamService;
import com.tanmu.service.SubjectService;
import com.tanmu.service.UserService;
import com.tanmu.util.RsaUtil;
import com.tanmu.util.TokenUtil;
import com.tanmu.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

@RestController //返回json格式
@RequestMapping("/exam") //user接口
public class ExamController {
    @Autowired
    private ExamService examService;
    @Autowired
    private UserService userService;
    @Autowired
    private SubjectService subjectService;
    @Value("${privateKey}")
    private String privateKey;
    @PostMapping("/createExam")
    public Result createExam(@RequestBody ExamVo examVo){
        String phone = examVo.getCreatorPhone();
        User user = userService.getUserByPhone(phone);
        examVo.setCreatorId(user.getId());//获取用户id
        Result result = examService.addExam(examVo);
        return result;
    }
    //添加试卷初始化
    @PostMapping("createExamDefault")
    public Result createExamDefault(@RequestBody String phone){
        phone = RsaUtil.decrypt(phone, privateKey);
        HashMap<String,Object> map=new HashMap<String,Object>();
        User user = userService.getUserByPhone(phone);
        if(user==null){
            return Result.error("500","用户信息不存在");
        }else{
            List<Subject> subjects = subjectService.queryAll();
            map.put("user",user);
            map.put("subjects",subjects);
            return Result.success(map);
        }
    }
    //根据id查询试卷信息
    @PostMapping("editExamDefault")
    public Result editExamDefault(@RequestBody String id){
        //解密
        id= RsaUtil.decrypt(id,privateKey);
        Result exam = examService.getExamById(Integer.parseInt(id));
        System.out.println(exam);
        return exam;
    }
    //分页查询所有试卷信息
    @GetMapping("/getExamAll")
    public Result<?> getExamAll(@RequestParam(defaultValue ="1") Integer pageNumber,@RequestParam(defaultValue = "10") Integer pageSize,@RequestParam(defaultValue = "") String search){
        //分页查询
        Page<ExamListVo> examAll = examService.getExamAll(pageNumber, pageSize, search);
        return Result.success(examAll);
    }
    //修改试卷是否发布
    @PostMapping("/updateReleaseYn/{id}/{yn}")
    public Result<?> updateReleaseYn(@PathVariable Integer id,@PathVariable Integer yn,HttpServletRequest request) throws UnsupportedEncodingException {
        String token = request.getHeader("token");
        String userPhone = TokenUtil.getUserPhone(token);
        User user = userService.getUserByPhone(userPhone);
        if(user==null){
            return Result.error("1007","token不存在！");
        }
        Result result = examService.updateReleaseYn(id, yn,user.getId());
        return result;
    }
    //删除试卷
    @DeleteMapping("/deleteExam/{id}")
    public Result<?> deleteExam(@PathVariable Integer id,HttpServletRequest request) throws UnsupportedEncodingException{
        String token = request.getHeader("token");
        String userPhone = TokenUtil.getUserPhone(token);
        User user = userService.getUserByPhone(userPhone);
        if(user==null){
            return Result.error("1007","token不存在！");
        }
        Result result = examService.deleteExam(id, user.getId());
        return result;
    }

    //修改试卷  @RequestBody User user
    @PostMapping("/updateExam")
    public Result<?> updateExam(@RequestBody ExamVo examVo,HttpServletRequest request) throws UnsupportedEncodingException {
        String token = request.getHeader("token");
        String userPhone = TokenUtil.getUserPhone(token);
        User user = userService.getUserByPhone(userPhone);
        if(user==null){
            return Result.error("1007","token不存在！");
        }
        //修改试卷
        Result result = examService.updateExam(examVo,user.getId());
       return result;
    }
    //用户获取试卷列表
    @GetMapping("/getUserExamAll")
    public Result<?> getUserExamAll(@RequestParam(defaultValue ="1") Integer pageNumber,@RequestParam(defaultValue = "10") Integer pageSize,@RequestParam(defaultValue = "") String search,
                                    @RequestParam(defaultValue = "") String userId,HttpServletRequest request) throws UnsupportedEncodingException {
        String token = request.getHeader("token");
        String userPhone = TokenUtil.getUserPhone(token);
        User user = userService.getUserByPhone(userPhone);
        if(user==null){
            return Result.error("1007","token不存在！");
        }
        //分页查询
        Page<UserExamListVo> userExamAll=null;
        if(userId.equals("")){
            userExamAll  = examService.getUserExamAll(pageNumber, pageSize, search,user.getId());
        }else{
            //解密
            String decryptUserId = RsaUtil.decrypt(userId, privateKey);
            userExamAll  = examService.getUserExamAll(pageNumber, pageSize, search,Integer.parseInt(decryptUserId));
        }
        return Result.success(userExamAll);
    }
    //管理员获取试卷列表
    @GetMapping("/getAdminExamAll")
    public Result<?> getAdminExamAll(@RequestParam(defaultValue ="1") Integer pageNumber,@RequestParam(defaultValue = "10") Integer pageSize,@RequestParam(defaultValue = "") String search,HttpServletRequest request) throws UnsupportedEncodingException {
        String token = request.getHeader("token");
        String userPhone = TokenUtil.getUserPhone(token);
        User user = userService.getUserByPhone(userPhone);
        if(user==null){
            return Result.error("1007","token不存在！");
        }
        //分页查询
        Page<AdminExamListVo> adminExamAll = examService.getAdminExamAll(pageNumber, pageSize, search);
        return Result.success(adminExamAll);
    }
}
