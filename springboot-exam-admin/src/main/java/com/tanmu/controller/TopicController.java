package com.tanmu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanmu.common.Result;
import com.tanmu.entity.Topic;
import com.tanmu.entity.User;
import com.tanmu.service.SubjectService;
import com.tanmu.service.TopicService;
import com.tanmu.service.UserService;
import com.tanmu.util.RsaUtil;
import com.tanmu.util.TokenUtil;
import com.tanmu.vo.ExamListVo;
import com.tanmu.vo.ExamVo;
import com.tanmu.vo.TopicVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController //返回json格式
@RequestMapping("/topic") //接口
public class TopicController {
    @Autowired
    private TopicService topicService;
    @Autowired
    private UserService userService;
    @Value("${privateKey}")
    private String privateKey;
    //分页查询所有题目信息
    @GetMapping("/getTopicAll")
    public Result<?> getUserAll(@RequestParam(defaultValue ="1") Integer pageNumber, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "") String search){
        //分页查询
        Page<TopicVo> page = topicService.getExamSubjectAll(pageNumber, pageSize, search);
        return Result.success(page);
    }
    //获取题目id
    @PostMapping("/getTopicById")
    public Result<?> getTopicById(@RequestBody String id){
        id=RsaUtil.decrypt(id,privateKey);
        Result topicById = topicService.getTopicById(Integer.parseInt(id));
        return topicById;
    }
    //修改试题
    @PutMapping("/updateTopic")
    public Result<?> updateTopic(@RequestBody ExamVo examVo, HttpServletRequest request) throws UnsupportedEncodingException {
        String token = request.getHeader("token");
        String userPhone = TokenUtil.getUserPhone(token);
        User user = userService.getUserByPhone(userPhone);
        if(user==null){
            return Result.error("1007","token不存在！");
        }
        List<Topic> topics = examVo.getTopics();
        if(topics.size()>0 && topics.size()<2){
            Topic topic = topics.get(0);
            Result result = topicService.updateTopic(topic, user.getId());
            return result;
        }else {
            return Result.error("500","题目为空！");
        }
    }

    //删除试题
    @DeleteMapping("/deleteTopic/{id}")
    public Result<?> deleteTopic(@PathVariable Integer id,HttpServletRequest request) throws UnsupportedEncodingException{
        String token = request.getHeader("token");
        String userPhone = TokenUtil.getUserPhone(token);
        User user = userService.getUserByPhone(userPhone);
        if(user==null){
            return Result.error("1007","token不存在！");
        }
        //执行删除
        Result result = topicService.deleteTopic(id, user.getId());
        return result;
    }
}
