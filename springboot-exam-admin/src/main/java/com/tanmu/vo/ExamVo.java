package com.tanmu.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tanmu.entity.Examination;
import com.tanmu.entity.Topic;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ExamVo {
    private Integer examId;//试卷id
    private String examName;//试卷名称
    private Integer creatorId;// 创建者id
    private String creatorPhone;//创建者手机号
    private String creatorName;//创建者名称
    private Integer time;// 答题时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;// 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateDate;//修改时间
    private Integer subjectId;//科目类型id
    private String subjectName;//科目类型名称
    private Integer topicNum;//题目数量
    private Float totalScore;//总分
    private Float passMark;//及格分数
    private Integer repeatTest;//允许考生考试次数 默认1
    private Integer releasing;//是否发布中 0:是  1:否
    private List<Topic> topics;//题目集合
}
