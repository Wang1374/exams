package com.tanmu.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@TableName("topic")
@Data
public class Topic {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer topicType;//题型类别
    private String question;//问题
    private String optionss;//选项
    private String photoPath;//图片路径
    private String answer;//答案
    private Float score;//分数
    private String analysis;//解析
    private String difficulty;//难度
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
    //自动填充 默认0   @TableLogic定义删除逻辑为1
    @TableLogic(value = "0", delval = "1")
    @TableField(fill = FieldFill.INSERT)
    private Integer deleteYn;
}
