package com.tanmu.controller;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.tanmu.common.Result;
import com.tanmu.entity.User;
import com.tanmu.entity.UserTopic;
import com.tanmu.service.UserService;
import com.tanmu.service.impl.UserGradeServiceImpl;
import com.tanmu.service.impl.UserTopicServiceImpl;
import com.tanmu.util.TokenUtil;
import com.tanmu.vo.UserGradeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController //返回json格式
@RequestMapping("/userGrade")
public class UserGradeController {
    private static final Log log = LogFactory.get();
    @Autowired
    private UserGradeServiceImpl userGradeService;
    @Autowired
    private UserTopicServiceImpl userTopicService;
    @Autowired
    private UserService userService;
    @PostMapping("/saveUserGrade")
    public Result<?> saveUserGrade(@RequestBody UserGradeVo userGradeVo,HttpServletRequest request) throws UnsupportedEncodingException {
        String token = request.getHeader("token");
        String userPhone = TokenUtil.getUserPhone(token);
        User user = userService.getUserByPhone(userPhone);
        if(user==null){
            return Result.error("1007","token不存在！");
        }
        //学生提交试卷------数据日志
        log.info("UserGradeController===============================学生提交试卷------数据日志==================================开始");
        log.info(userGradeVo.getInfo());
        //题目数据
        List<UserTopic> userTopicList = userGradeVo.getUserTopicList();
        for (UserTopic userTopic : userTopicList) {
            log.info(userTopic.getInfo());
        }
        log.info("===============================学生提交试卷------数据日志==================================结束");
        Result result = userGradeService.insertUserGrade(userGradeVo);
        return result;
    }
}
