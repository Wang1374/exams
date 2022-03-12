package com.tanmu.vo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class UserExamListVo {
    private Integer id;
    private Integer userGradeId;//成绩单id
    private String examName;//试卷名称
    private Integer AnswerTime;//答题时间
    private Integer subjectId;//科目类型id
    private String subjectName;//类型名称
    private Float examScore;//试卷总分
    private Float passingScore;//及格分数
    private Float grade;//得分
    private Integer examHsTime;//考试耗时时长
    private Integer topicNum;//题目数量
    private Integer examStatus;//试卷状态 0:未完成  1:批改中 2:已完成
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8") //返回日期格式
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//接收日期格式
    private Date answerDate;//答题日期
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8") //返回日期格式
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//接收日期格式
    private Date markDate;     //批改日期
}
