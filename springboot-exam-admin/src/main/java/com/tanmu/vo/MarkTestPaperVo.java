package com.tanmu.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
@Data
public class MarkTestPaperVo {
    private Integer examId;//试卷id
    private String examName;//试卷名称
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8") //返回日期格式
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//接收日期格式
    private Date createTime;//创建时间
    private Integer AnswerTime;//答题时间
    private Integer creatorId;// 创建者id
    private String creatorName;//创建者名称
    private Integer subjectId;//科目类型id
    private String subjectName;//科目类型名称
    private Integer topicNum;//题目数量
    private Float totalScore;//总分
    private Float passMark;//及格分数
    private Integer userId;//答题者id
    private String userName;//答题者名字
    private Double grade;//总得分
    private Double gradeAuto;//自动评分
    private Integer examHsTime;//考试耗时时长
    private Integer markStatus;//批改状态 0:待批改 1:批改完成
    private Integer examStatus;//试卷状态 0:未完成  1:批改中 2:已完成
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8") //返回日期格式
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//接收日期格式
    private Date answerDate;//答题日期
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8") //返回日期格式
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//接收日期格式
    private Date markDate;     //批改日期
    private List<UserTopicVo> userTopicVos;//题目集合
}
