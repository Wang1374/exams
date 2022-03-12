package com.tanmu.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@ToString
@TableName("examination")
public class Examination {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String examName;//试卷名称
    private Integer AnswerTime;//答题时间
    private Integer subjectId;//科目类型id
    private Float examScore;//试卷总分
    private Float passingScore;//及格分数
    private Integer topicNum;//题目数量
    private Integer createUserId;//创建人id
    //创建时间 自动填充策略
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8") //返回日期格式
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//接收日期格式
    private Date createTime;//创建时间
    private Integer updateUserId;//修改人id
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8") //返回日期格式
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//接收日期格式
    private Date updateTime;//修改时间
    @TableField(fill = FieldFill.INSERT)
    private Integer releaseYn;//是否发布
    //自动填充 默认0   @TableLogic定义删除逻辑为1
    @TableLogic(value = "0", delval = "1")
    @TableField(fill = FieldFill.INSERT)
    private Integer deleteYn;
}
