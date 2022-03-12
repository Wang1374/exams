package com.tanmu.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class UserVo {
    private String phone;
    private String oldPassWord;
    private String newPassWord;
}
