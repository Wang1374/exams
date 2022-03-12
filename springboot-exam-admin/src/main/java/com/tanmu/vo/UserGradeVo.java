package com.tanmu.vo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tanmu.entity.UserTopic;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class UserGradeVo {
    private Integer id;
    private Integer userId;
    private Integer examId;
    private String examName;
    private Double grade;//得分
    private Double gradeAuto;//自动评分
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8") //返回日期格式
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//接收日期格式
    private Date answerDate;//答题日期
    private Integer answerTime;//答题时长
    private Integer markStatus;//批改状态 0:待批改 1:批改完成
    private Integer examStatus;//试卷状态 0:未完成  1:批改中 2:已完成
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8") //返回日期格式
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//接收日期格式
    private Date markDate;     //批改日期
    private Integer deleteYn;
    //用户答题数组
    private List<UserTopic> userTopicList;


    public String getInfo() {
        return "UserGradeVo{" +
                "id=" + id +
                ", userId=" + userId +
                ", examId=" + examId +
                ", examName='" + examName + '\'' +
                ", grade=" + grade +
                ", gradeAuto=" + gradeAuto +
                ", answerDate=" + answerDate +
                ", answerTime=" + answerTime +
                ", markStatus=" + markStatus +
                ", examStatus=" + examStatus +
                ", markDate=" + markDate +
                ", deleteYn=" + deleteYn +
                ", userTopicList=" + userTopicList +
                '}';
    }
}
