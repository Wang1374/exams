package com.tanmu.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    //添加数据时自动填充
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "registrationTime", Date.class, new Date());
        this.strictInsertFill(metaObject,"deleteYn",Integer.class,0);// 0 未删除 1 已删除
        this.strictInsertFill(metaObject,"useYn",Integer.class,0);//0 使用中 1禁用
        this.strictInsertFill(metaObject,"role",Integer.class,2);// 0 超级管理员 1管理员 2 普通用户
        this.strictInsertFill(metaObject,"number",Integer.class,0);//0 密码输入错误次数
        this.strictInsertFill(metaObject,"password",String.class,"123456");//默认密码123456
        this.strictInsertFill(metaObject,"age",Integer.class,18);//默认年龄18
        this.strictInsertFill(metaObject,"sex",Integer.class,1);//默认男
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());//创建时间
        this.strictInsertFill(metaObject, "releaseYn", Integer.class, 1);//默认1   0发布  1未发布
    }
   //修改数据时自动填充
    @Override
    public void updateFill(MetaObject metaObject) {
        //this.strictUpdateFill(metaObject, "registrationTime", Date.class, new Date());
    }
}