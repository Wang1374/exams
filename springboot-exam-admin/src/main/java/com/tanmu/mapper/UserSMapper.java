package com.tanmu.mapper;

import com.tanmu.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserSMapper {
    //查询软删除用户是否存在
    User getIsDeleteUser(String phone);
    //彻底删除已经被软删除用户
    void deleteUser(String phone);
}
