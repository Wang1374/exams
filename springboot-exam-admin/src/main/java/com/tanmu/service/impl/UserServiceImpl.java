package com.tanmu.service.impl;



import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanmu.common.Result;
import com.tanmu.entity.Examination;
import com.tanmu.mapper.UserMapper;
import com.tanmu.entity.User;
import com.tanmu.mapper.UserSMapper;
import com.tanmu.service.UserService;
import com.tanmu.util.HMACSHA256;
import com.tanmu.util.RsaUtil;
import com.tanmu.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.UUID;

@Service
@Transactional //事务注解
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Value("${privateKey}")
    private String privateKey;
    @Value("${resetPassword}")
    private String resetPassword;
    @Autowired
    private UserSMapper userSMapper;
   //登录方法 登录成功返回token 登录失败返回null
    @Override
    public Result login(User user) {
        //解密
        String phone = RsaUtil.decrypt(user.getPhone(), privateKey);
        String password = RsaUtil.decrypt(user.getPassword(), privateKey);
        //解密后数据再次写入对象中
        user.setPhone(phone);
        user.setPassword(password);
        try {
            User user1 = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getPhone, user.getPhone()));

            if (user1 == null) {
                return Result.error("1000","账户不存在！");
            }

            if(user1.getUseYn()==1){
                return Result.error("1002","账户被禁用,请联系管理员解除！");
            }

            String pass = HMACSHA256.sha256_HMAC(user.getPassword(), user1.getKeyUuid());
            if(!pass.equals(user1.getPassword())){
                //错误一次 密码次数+1
                user1.setNumber(user1.getNumber()+1);
                if(user1.getNumber()>=5){
                   //锁定账户
                    user1.setUseYn(1);
                    userMapper.updateById(user1);
                    return Result.error("1002","账户被禁用,请联系管理员解除！");
                }else {
                    userMapper.updateById(user1);
                    return Result.error("1001", "密码错误五次将锁定账户,还剩" + (5 - user1.getNumber()) + "次！");
                }
            }else{
                user1.setNumber(0);
                userMapper.updateById(user1);
            }
            String token = TokenUtil.getToken(user1);
            return Result.success(token);
        }catch (Exception e){
            //强制手动事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return Result.error("500","系统异常！");
        }
    }

    @Override
    public boolean checkUserByPhone(String phone) {
        User user2 = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getPhone, phone));
        if (user2 == null) {
            return false;
        }
        return true;
    }

    @Override
    public Page<User> getUserAll(Integer pageNumber, Integer pageSize, String search) {
        Page<User> userPage = new Page<>(pageNumber, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //判断search条件是否为空
        if(StrUtil.isNotBlank(search)){
            //构造条件 模糊查询
            queryWrapper.like("name",search);
        }
        Page<User> page = userMapper.selectPage(userPage, queryWrapper);
        return page;
    }


    @Override
    public Result saveUser(User user) {
        try {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String password = user.getPassword();
            user.setKeyUuid(uuid);
            if (password==null){
                //设置默认密码
                password= resetPassword;
            }
            String keyPass = HMACSHA256.sha256_HMAC(password, uuid);
            user.setPassword(keyPass);
            //判断用户是否被删除 如果被软删除 再次注册需要彻底删除
            User isDeleteUser = userSMapper.getIsDeleteUser(user.getPhone());
            if(isDeleteUser!=null){
                //执行强制删除
                System.out.println("存在已经被软删除用户执行强制删除");
                userSMapper.deleteUser(user.getPhone());
            }
            userMapper.insert(user);
            return Result.success();
        }catch (Exception e){
            //强制手动事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return Result.error("500","用户注册失败！");
        }
    }

    @Override
    public int updateUser(User user) {
        int code=0;
        try {
            code= userMapper.updateById(user);
        }catch (Exception e){
            //强制手动事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
        }
        return code;
    }

    @Override
    public int deleteUser(Integer id) {
        int code=0;
        try {
            code= userMapper.deleteById(id);
        }catch (Exception e){
            //强制手动事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
        }
        return code;
    }

    @Override
    public User getUserByPhone(String phone) {
        User user3 = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getPhone, phone));
        if (user3 == null) {
            return null;
        }
        return user3;
    }

    @Override
    public Result updateUserYn(Integer id, Integer yn) {
        User user = userMapper.selectById(id);
        try {
            if(user!=null){
                user.setUseYn(yn);
                userMapper.updateById(user);
            }
            return Result.success();
        }catch (Exception e){
            //强制手动事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.error("500","用户状态修改失败");
        }
    }
    @Override
    public User getNickNameAndPhone(String nickName, String phone) {
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getPhone, phone).eq(User::getNickName, nickName));
        return user;
    }

    @Override
    public boolean getIsDeleteUser(String phone) {
        User user = userSMapper.getIsDeleteUser(phone);
        if(user!=null){
            return true;
        }
        return false;
    }

    @Override
    public Result deleteUser(String phone) {
        try {
            userSMapper.deleteUser(phone);
            return Result.success();
        }catch (Exception e){
            //强制手动事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.error("500","用户删除失败");
        }

    }
}
