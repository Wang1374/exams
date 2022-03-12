package com.tanmu.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Data
public class RegisterUser {
    private String name;
    private String nickName;
    private String age;
    private String sex;
    private String phone;
    private String password;
    private String keyUuid;
    private Integer number;
    private String address;
    private String code;//验证码
}
