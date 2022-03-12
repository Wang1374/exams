package com.tanmu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanmu.common.Result;
import com.tanmu.entity.Topic;
import com.tanmu.vo.SubjectVo;
import com.tanmu.vo.TopicVo;

public interface TopicService {
    //获取所有题目列表
    Page<TopicVo> getExamSubjectAll(Integer pageNumber, Integer pageSize, String search);
    //删除题目
    Result deleteTopic(Integer topicId,Integer userId);
    //修改题目
    Result updateTopic(Topic topic, Integer userId);
    //根据id获取题目
    Result getTopicById(Integer id);
}
