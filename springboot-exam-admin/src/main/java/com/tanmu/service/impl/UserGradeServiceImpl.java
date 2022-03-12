package com.tanmu.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanmu.common.Result;
import com.tanmu.entity.*;
import com.tanmu.mapper.ExaminationMapper;
import com.tanmu.mapper.TopicMapper;
import com.tanmu.mapper.UserGradeMapper;
import com.tanmu.mapper.UserTopicMapper;
import com.tanmu.service.UserGradeService;
import com.tanmu.util.TokenUtil;
import com.tanmu.util.TopicUtil;
import com.tanmu.vo.UserGradeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.List;

@Service
@Transactional //事务注解
public class UserGradeServiceImpl implements UserGradeService {
    private static final Log log = LogFactory.get();
    @Autowired
    private UserGradeMapper userGradeMapper;
     @Autowired
     private TopicMapper topicMapper;
     @Autowired
     private UserTopicMapper userTopicMapper;
     @Autowired
     private ExaminationMapper examinationMapper;
    @Override
    public Result insertUserGrade(UserGradeVo userGradeVo) {
        log.info("UserGradeServiceImpl===============================写入用户试卷数据==================================开始");
        //判断试卷提交是否正确
        Integer examId = userGradeVo.getExamId();
        Examination examination = examinationMapper.selectById(examId);
        //试卷名字对不上  提交错误  返回错误
        if(!(examination.getExamName().equals(userGradeVo.getExamName()))){
            log.info("UserGradeServiceImpl==============================试卷提交错误,重复提交，试卷名称不一致==================================开始");
            return Result.error("500","试卷提交错误,重复提交！");
        }
        int flage=1;//定义标识符
        try {
            UserGrade userGrade = new UserGrade();
            BeanUtils.copyProperties(userGradeVo,userGrade);
            userGrade.setAnswerDate(new Date());//写入答题时间
            userGrade.setMarkStatus(0);//批改状态 0:待批改 1:批改完成
            userGrade.setExamStatus(1);//试卷状态 0:未完成  1:批改中 2:已完成
            //自动评分
            double scoreAuto=0.0;
            //单项选择题和多选题以及判断题题目进入自动判卷
            //------------------自动判卷---------------------
            List<UserTopic> userTopicList = userGradeVo.getUserTopicList();
            for (UserTopic userTopic : userTopicList) {
                Integer topicType = userTopic.getTopicType();
                if(topicType==0 || topicType==2){
                    Integer topicId = userTopic.getTopicId();
                    Topic topic = topicMapper.selectById(topicId);
                    double score = topic.getScore();
                    String answer = topic.getAnswer();
                    String userAnswer = userTopic.getUserAnswer();
                    if(topic!=null){
                        if(answer.equals(userAnswer)){
                            //答案正确则写入分数
                            scoreAuto+=score;
                            userTopic.setUserScore(score);
                            userTopic.setTopicStatus(1);
                        }else{
                            userTopic.setUserScore(0.0);
                            userTopic.setTopicStatus(1);
                        }
                    }else{
                        //题目为空
                        userTopic.setTopicStatus(0);
                    }
                }else if(topicType==1){
                    //多选题  判卷规则选对得分  选错不得分 选不完全 得总分值的半分
                    Integer topicId = userTopic.getTopicId();
                    Topic topic = topicMapper.selectById(topicId);
                    double score = topic.getScore();
                    int i = TopicUtil.ifTopic(userTopic.getUserAnswer(), topic.getAnswer());
                    //多选题单个选项补下划线
                    String[] userAnswers = userTopic.getUserAnswer().split("\\|");
                    if(userAnswers.length<2){
                        userTopic.setUserAnswer(userTopic.getUserAnswer()+"|");
                    }
                    if(i==2){
                        //完全正确
                        scoreAuto+=score;
                        userTopic.setUserScore(score);
                        userTopic.setTopicStatus(1);
                    }else if (i==1){
                        //正确一半  给一半分
                        scoreAuto+=(score/2);
                        userTopic.setUserScore(score/2);
                        userTopic.setTopicStatus(1);
                    }else{
                        //错误 零分
                        userTopic.setUserScore(0.0);
                        userTopic.setTopicStatus(1);
                    }
                }else {
                    //写入状态为未批改
                    userTopic.setTopicStatus(0);
                }
                //写入数据到用户题表
                log.info("===============================写入第"+(flage)+"题到用户题表==================================");
                log.info(userTopic.getInfo());
                userTopicMapper.insert(userTopic);
                flage++;
            }
            //----------------------------------------------
            //写入自动评分
            log.info("===============================写入自动评分最终数据到数据库==================================");
            userGrade.setGradeAuto(scoreAuto);
            log.info(userGrade.getInfo());
            userGradeMapper.insert(userGrade);
            log.info("UserGradeServiceImpl===============================写入用户试卷数据==================================结束");
            return Result.success();
        }catch (Exception e){
            log.info("UserGradeServiceImpl===============================数据异常=================================="+"第"+flage+"题提交失败,请检查答案是否符合规范！");
            //强制手动事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return Result.error("500","第"+flage+"题提交失败,请检查答案是否符合规范！");
        }
    }

    @Override
    public Result getUserGrade(Integer id) {
        UserGrade userGrade = userGradeMapper.selectById(id);
        return Result.success(userGrade);
    }

    @Override
    public Result getUserGradeByUserId(Integer userId) {
        List<UserGrade> userGrades = userGradeMapper.selectList(Wrappers.<UserGrade>lambdaQuery().eq(UserGrade::getUserId, userId));
        return Result.success(userGrades);
    }

    @Override
    public Result getUserGradeByExamId(Integer examId) {
        List<UserGrade> userGrades = userGradeMapper.selectList(Wrappers.<UserGrade>lambdaQuery().eq(UserGrade::getExamId, examId));
        return Result.success(userGrades);
    }

    @Override
    public Page<UserGrade> getUserAll(Integer pageNumber, Integer pageSize, String search) {
        Page<UserGrade> userGradePage = new Page<>(pageNumber, pageSize);
        QueryWrapper<UserGrade> queryWrapper = new QueryWrapper<>();
        //判断search条件是否为空
        if(StrUtil.isNotBlank(search)){
            //构造条件 模糊查询
            queryWrapper.like("exam_name",search);
        }
        Page<UserGrade> gradePage = userGradeMapper.selectPage(userGradePage, queryWrapper);
        return gradePage;
    }

    @Override
    public Result updateUserGrade(UserGradeVo userGradeVo) {
        try {
            int insert = userGradeMapper.updateById(null);
            return Result.success(insert);
        }catch (Exception e){
            //强制手动事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return Result.error("500","修改失败！");
        }
    }

    @Override
    public Result deleteUserGrade(Integer id) {
        try {
            int insert = userGradeMapper.deleteById(id);
            return Result.success(insert);
        }catch (Exception e){
            //强制手动事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return Result.error("500","删除失败！");
        }
    }
}
