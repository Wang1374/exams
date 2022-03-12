package com.tanmu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanmu.common.Result;
import com.tanmu.entity.User;

public interface UserService {
     //登录方法
     Result login(User user);
     //根据手机号查询用户是否存在
     boolean checkUserByPhone(String phone);
     //获取所有用户信息 分页形式
     Page<User> getUserAll(Integer pageNumber, Integer pageSize, String search);
     //保存用户
     Result saveUser(User user);
     //修改用户信息
     int updateUser(User user);
     //根据id删除用户信息
     int deleteUser(Integer id);
     //根据手机号查询用户信息
     User getUserByPhone(String phone);
     //修改试卷是否发布
     Result updateUserYn(Integer id,Integer yn);
     //根据手机号和姓名查找用户
     User getNickNameAndPhone(String nickName,String phone);
     //查询软删除用户是否存在
     boolean getIsDeleteUser(String phone);
     //彻底删除已经被软删除用户
     Result deleteUser(String phone);
}
