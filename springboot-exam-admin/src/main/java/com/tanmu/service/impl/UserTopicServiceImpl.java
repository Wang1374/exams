package com.tanmu.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tanmu.common.Result;
import com.tanmu.entity.UserTopic;
import com.tanmu.mapper.UserTopicMapper;
import com.tanmu.service.UserTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Service
@Transactional //事务注解
public class UserTopicServiceImpl implements UserTopicService {
    @Autowired
    private UserTopicMapper userTopicMapper;
    @Override
    public Result insertUserTopic(UserTopic userTopic) {
        try {
            int insert = userTopicMapper.insert(userTopic);
            return Result.success(insert);
        }catch (Exception e){
            //强制手动事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return Result.error("500","学生答案保存失败！");
        }
    }

    @Override
    public Result updateUserTopic(UserTopic userTopic) {
        try {
            int insert = userTopicMapper.updateById(userTopic);
            return Result.success(insert);
        }catch (Exception e){
            //强制手动事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return Result.error("500","修改失败！");
        }
    }

    @Override
    public Result deleteUserTopic(Integer id) {
        try {
            int insert = userTopicMapper.deleteById(id);
            return Result.success(insert);
        }catch (Exception e){
            //强制手动事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return Result.error("500","删除失败！");
        }
    }

    @Override
    public Result selectUserTopicByExamId(Integer examId) {
        List<UserTopic> userTopics = userTopicMapper.selectList(Wrappers.<UserTopic>lambdaQuery().eq(UserTopic::getExamId, examId));
        return Result.success(userTopics);
    }
}
