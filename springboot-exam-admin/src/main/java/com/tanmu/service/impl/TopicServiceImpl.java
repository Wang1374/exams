package com.tanmu.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanmu.common.Result;
import com.tanmu.entity.*;
import com.tanmu.mapper.ExaminationMapper;
import com.tanmu.mapper.ReationMapper;
import com.tanmu.mapper.TopicMapper;
import com.tanmu.mapper.UserMapper;
import com.tanmu.service.TopicService;
import com.tanmu.vo.SubjectVo;
import com.tanmu.vo.TopicVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
@Transactional //事务注解
public class TopicServiceImpl implements TopicService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private ReationMapper reationMapper;
    @Autowired
    private ExaminationMapper examinationMapper;
    @Override
    public Page<TopicVo> getExamSubjectAll(Integer pageNumber, Integer pageSize, String search) {
        Page<TopicVo> topicVoPage=new Page<>();
        List<TopicVo> list=new ArrayList<>();
        Page<Topic> topicPage = new Page<>(pageNumber, pageSize);
        QueryWrapper<Topic> queryWrapper = new QueryWrapper<>();
        //判断search条件是否为空
        if(StrUtil.isNotBlank(search)){
            //构造条件 模糊查询
            queryWrapper.like("question",search);
        }
        Page<Topic> page = topicMapper.selectPage(topicPage, queryWrapper);
        List<Topic> records = page.getRecords();
        for (Topic record : records) {
            TopicVo topicVo = new TopicVo();
            BeanUtils.copyProperties(record,topicVo);
            Integer createUserId = record.getCreateUserId();
            Integer updateUserId = record.getUpdateUserId();
            User createUser = userMapper.selectById(createUserId);
            User updateUser = userMapper.selectById(updateUserId);
            topicVo.setCreateUserName(createUser.getName());
            topicVo.setUpdateUserName(updateUser.getName());
            list.add(topicVo);
        }
        topicVoPage.setRecords(list);
        topicVoPage.setTotal(topicPage.getTotal());
        return topicVoPage;
    }

    @Override
    public Result deleteTopic(Integer topicId, Integer userId) {
        try {
            //试卷总分  分数减去题目分数  数量也是-1
            Reation reation = reationMapper.selectOne(Wrappers.<Reation>lambdaQuery().eq(Reation::getTopicId, topicId));
            Integer examId = reation.getExamId();
            Examination examination = examinationMapper.selectById(examId);
            Topic topic = topicMapper.selectById(topicId);
            //修改分数
            examination.setExamScore(examination.getExamScore()-topic.getScore());
            examination.setUpdateUserId(userId);
            examination.setUpdateTime(new Date());
            examination.setTopicNum(examination.getTopicNum()-1);
            examinationMapper.updateById(examination);
            //删除题目
            topicMapper.deleteById(topicId);
            //删除前删除关系表
            reationMapper.delete(Wrappers.<Reation>lambdaQuery().eq(Reation::getTopicId, topicId));
            return Result.success();
        } catch (Exception e) {
            //强制手动事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return Result.error("500", "删除异常");
        }
    }

    @Override
    public Result updateTopic(Topic topic, Integer userId) {
        try {
            //修改分数 判断分数是否与原来一致
            Float score = topic.getScore();
            Topic topicMysql = topicMapper.selectById(topic.getId());
            if(score!=topicMysql.getScore()){
                //修改试卷分数
                Reation reation = reationMapper.selectOne(Wrappers.<Reation>lambdaQuery().eq(Reation::getTopicId, topic.getId()));
                Examination examination = examinationMapper.selectById(reation.getExamId());
                examination.setExamScore(examination.getExamScore()+(score-topicMysql.getScore()));
                examinationMapper.updateById(examination);
            }
            //修改试题数据
            topic.setUpdateTime(new Date());
            topic.setUpdateUserId(userId);
            topicMapper.updateById(topic);
            return Result.success();
        }catch (Exception e){
            //强制手动事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return Result.error("500","保存异常");
        }

    }

    @Override
    public Result getTopicById(Integer id) {
        Topic topic = topicMapper.selectById(id);
        if(topic!=null){
            return Result.success(topic);
        }else{
            return Result.error("500","题目不存在！");
        }

    }
}
