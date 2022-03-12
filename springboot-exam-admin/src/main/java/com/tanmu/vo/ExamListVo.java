package com.tanmu.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ExamListVo {
    private Integer id;
    private String examName;//试卷名称
    private Integer AnswerTime;//答题时间
    private Integer subjectId;//科目类型id
    private String subjectName;//类型名称
    private Float examScore;//试卷总分
    private Float passingScore;//及格分数
    private Integer topicNum;//题目数量
    private Integer createUserId;//创建人id
    private String createUserName;//创建者名字
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8") //返回日期格式
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//接收日期格式
    private Date createTime;//创建时间
    private Integer updateUserId;//修改人id
    private String updateUserName;//修改人名字
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8") //返回日期格式
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//接收日期格式
    private Date updateTime;//修改时间
    private Integer releaseYn;//是否发布
}
