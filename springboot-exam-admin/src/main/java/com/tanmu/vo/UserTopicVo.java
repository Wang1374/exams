package com.tanmu.vo;

import com.tanmu.entity.Topic;
import lombok.Data;

@Data
public class UserTopicVo {
    private Integer id;
    private Integer userId;
    private Integer examId;
    private Integer topicId;
    private Integer topicType;
    private String userAnswer;
    private Double userScore;
    private Integer topicStatus;
    private Topic topic;
}
