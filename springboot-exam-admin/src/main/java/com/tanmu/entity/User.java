package com.tanmu.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@TableName("user")
@Data
public class User {
    @TableId(type = IdType.AUTO) //设置主键自增
    private Integer id;
    private String name;
    private String nickName;
    @TableField(fill = FieldFill.INSERT)
    private Integer age;
    @TableField(fill = FieldFill.INSERT)
    private Integer sex;
    private String phone;
    private String password;
    private String keyUuid;
    @TableField(fill = FieldFill.INSERT)
    private Integer number;
    private String address;
    @TableField(fill = FieldFill.INSERT)
    private Integer role;
    @TableField(fill = FieldFill.INSERT)
    private Integer useYn;
    //自动填充 默认0   @TableLogic定义删除逻辑为1
    @TableLogic(value = "0", delval = "1")
    @TableField(fill = FieldFill.INSERT)
    private Integer deleteYn;
    //注册时间 自动填充策略
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8") //返回日期格式
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//接收日期格式
    private Date registrationTime;
}
