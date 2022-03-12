package com.tanmu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanmu.common.Result;
import com.tanmu.entity.Subject;
import com.tanmu.entity.User;
import com.tanmu.service.ExamService;
import com.tanmu.service.SubjectService;
import com.tanmu.service.UserService;
import com.tanmu.util.TokenUtil;
import com.tanmu.vo.SubjectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@RestController //返回json格式
@RequestMapping("/subject") //接口
public class SubjectController {
    @Autowired
    private UserService userService;
    @Autowired
    private SubjectService subjectService;

    //获取试卷类型
    @GetMapping("/getExamSubjectAll")
    public Result<?> getExamSubjectAll(@RequestParam(defaultValue ="1") Integer pageNumber, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "") String search){
        //分页查询
        Page<SubjectVo> examSubjectAll = subjectService.getExamSubjectAll(pageNumber, pageSize, search);
        return Result.success(examSubjectAll);
    }

    //修改试卷类型名称
    @PutMapping("/updateSubjectName")
    public Result<?> updateSubjectName(@RequestBody SubjectVo subjectVo, HttpServletRequest request) throws UnsupportedEncodingException {
        //判断是否存在同类型
        Subject subject = subjectService.selectByName(subjectVo.getSubjectName());
        if(subject!=null){
            return Result.error("1008","类型已经存在,请重新修改！");
        }
        String token = request.getHeader("token");
        String userPhone = TokenUtil.getUserPhone(token);
        User user = userService.getUserByPhone(userPhone);
        if(user==null){
            return Result.error("1007","token不存在！");
        }
        int i = subjectService.updateSubjectName(subjectVo,user.getId());
        if(i==1){
            return Result.success();
        }else{
            return Result.error("500","修改失败！");
        }
    }
    //删除试卷类型
    @DeleteMapping("/deleteSubject/{id}")
    public Result<?> deleteSubject(@PathVariable Integer id,HttpServletRequest request) throws UnsupportedEncodingException {
        String token = request.getHeader("token");
        String userPhone = TokenUtil.getUserPhone(token);
        User user = userService.getUserByPhone(userPhone);
        if(user==null){
            return Result.error("1007","token不存在！");
        }
        int i = subjectService.deleteSubject(id, user.getId());
        if(i==1){
            return Result.success();
        }else if(i==-1){
            return Result.error("500","删除失败,存在试卷正在使用此类型！");
        }else {
            return Result.error("500","删除失败！");
        }
    }
    //新增试卷类型
    @PostMapping("/addSubject/{subjectName}")  //?表示任意一种类型
    public Result<?> addSubject(@PathVariable String subjectName,HttpServletRequest request) throws UnsupportedEncodingException {
        //判断是否存在同类型
        Subject subject = subjectService.selectByName(subjectName);
        if(subject!=null){
            return Result.error("1008","类型已经存在,请勿重新添加！");
        }
        String token = request.getHeader("token");
        String userPhone = TokenUtil.getUserPhone(token);
        User user = userService.getUserByPhone(userPhone);
        if(user==null){
            return Result.error("1007","token不存在！");
        }
        subjectService.addSubject(subjectName, user.getId());
        return Result.success();
    }
}
