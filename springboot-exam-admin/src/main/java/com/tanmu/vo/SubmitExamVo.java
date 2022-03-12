package com.tanmu.vo;

import com.tanmu.entity.UserGrade;
import com.tanmu.entity.UserTopic;
import lombok.Data;

import java.util.List;

@Data
public class SubmitExamVo {
    private Integer examId;
    private Double grade;
    private Integer userId;
    private List<UserTopic> userTopicList;
}
