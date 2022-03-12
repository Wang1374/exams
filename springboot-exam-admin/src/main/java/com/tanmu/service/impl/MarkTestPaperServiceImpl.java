package com.tanmu.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tanmu.common.Result;
import com.tanmu.entity.*;
import com.tanmu.mapper.*;
import com.tanmu.service.MarkTestPaperService;
import com.tanmu.service.UserGradeService;
import com.tanmu.vo.MarkTestPaperVo;
import com.tanmu.vo.SubmitExamVo;
import com.tanmu.vo.UserTopicVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class MarkTestPaperServiceImpl implements MarkTestPaperService {
    @Autowired
    private UserGradeMapper userGradeMapper;
    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private UserTopicMapper userTopicMapper;
    @Autowired
    private ExaminationMapper examinationMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SubjectMapper subjectMapper;
    @Override
    public Result getUserExamById(Integer user_gradeId) {
        List<UserTopicVo> userTopicVos  = new ArrayList<>();
        MarkTestPaperVo markTestPaperVo = new MarkTestPaperVo();
        UserGrade   userGrade           = userGradeMapper.selectById(user_gradeId);
        Integer     userId              = userGrade.getUserId();
        Integer     examId              = userGrade.getExamId();
        User        user                = userMapper.selectById(userId);
        Examination examination         = examinationMapper.selectById(examId);
        Integer     subjectId           = examination.getSubjectId();
        Subject     subject             = subjectMapper.selectById(subjectId);
        //根据试卷id和用户id 查询出题目集合
        List<UserTopic> userTopics = userTopicMapper.selectList(Wrappers.<UserTopic>lambdaQuery().eq(UserTopic::getExamId, examId).eq(UserTopic::getUserId, userId));
        for (UserTopic userTopic : userTopics) {
            UserTopicVo userTopicVo         = new UserTopicVo();
            //拷贝userTopic数据到UserTopicVo
            BeanUtils.copyProperties(userTopic,userTopicVo);
            Integer topicId = userTopic.getTopicId();
            //获取题目
            Topic topic = topicMapper.selectById(topicId);
            userTopicVo.setTopic(topic);
            userTopicVos.add(userTopicVo);
        }
        //添加最终数据
          BeanUtils.copyProperties(examination,markTestPaperVo);//试卷信息
          BeanUtils.copyProperties(userGrade,markTestPaperVo);//用户成绩单
          markTestPaperVo.setAnswerTime(examination.getAnswerTime());//考试时间
          markTestPaperVo.setExamHsTime(userGrade.getAnswerTime());//考试消耗时间
          markTestPaperVo.setCreatorId(examination.getCreateUserId());//出卷者id
          User user1 = userMapper.selectById(examination.getCreateUserId());//获取出卷者
          markTestPaperVo.setCreatorName(user1.getName());//出卷者姓名
          markTestPaperVo.setCreateTime(examination.getCreateTime());//试卷创建时间
          markTestPaperVo.setUserId(userId);//答题人id
          markTestPaperVo.setUserName(user.getName());//答题人姓名
          markTestPaperVo.setUserTopicVos(userTopicVos);//答题集合
          markTestPaperVo.setSubjectId(subjectId);//类别id
          markTestPaperVo.setSubjectName(subject.getSubjectName());//类别名称
          markTestPaperVo.setTotalScore(examination.getExamScore());//试卷总分
          markTestPaperVo.setPassMark(examination.getPassingScore());//及格分数
        return Result.success(markTestPaperVo);//返回数据
    }

    @Override
    public Result saveExam(SubmitExamVo submitExamVo) {
        Integer examId = submitExamVo.getExamId();
        Double grade = submitExamVo.getGrade();
        Integer userId = submitExamVo.getUserId();
        List<UserTopic> userTopicList = submitExamVo.getUserTopicList();
        UserGrade userGrade = new UserGrade();
        userGrade.setGrade(grade);
        userGrade.setMarkStatus(1);
        userGrade.setMarkDate(new Date());
        userGrade.setExamStatus(2);
        try {
            //保存试卷
            userGradeMapper.update(userGrade,Wrappers.<UserGrade>lambdaQuery().eq(UserGrade::getUserId,userId).eq(UserGrade::getExamId,examId));
            //保存试题
            for (UserTopic userTopic : userTopicList) {
                Integer topicId = userTopic.getTopicId();
                userTopic.setTopicStatus(1);
                userTopicMapper.update(userTopic,Wrappers.<UserTopic>lambdaQuery().eq(UserTopic::getTopicId,topicId).eq(UserTopic::getUserId,userId).eq(UserTopic::getExamId,examId));
            }
            return Result.success();
        }catch (Exception e){
            //强制手动事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return Result.error("500","试卷保存异常");
        }
    }
}
