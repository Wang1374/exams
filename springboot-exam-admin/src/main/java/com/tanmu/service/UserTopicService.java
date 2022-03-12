package com.tanmu.service;

import com.tanmu.common.Result;
import com.tanmu.entity.UserTopic;

public interface UserTopicService {
    //添加用户答题
    Result insertUserTopic(UserTopic userTopic);
    //修改答题
    Result updateUserTopic(UserTopic userTopic);
    //删除答题
    Result deleteUserTopic(Integer id);
    //根据试卷id查询答题
    Result selectUserTopicByExamId(Integer examId);
}
