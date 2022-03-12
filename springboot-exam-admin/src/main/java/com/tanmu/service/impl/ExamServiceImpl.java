package com.tanmu.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanmu.common.Result;
import com.tanmu.entity.*;
import com.tanmu.mapper.*;
import com.tanmu.service.ExamService;
import com.tanmu.vo.AdminExamListVo;
import com.tanmu.vo.ExamListVo;
import com.tanmu.vo.ExamVo;
import com.tanmu.vo.UserExamListVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

@Service
@Transactional //事务注解
public class ExamServiceImpl implements ExamService {
    @Autowired
    private ExaminationMapper examinationMapper;
    @Autowired
    private ReationMapper reationMapper;
    @Autowired
    private SubjectMapper subjectMapper;
    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserGradeMapper userGradeMapper;
    @Override
    public Result addExam(ExamVo examVo) {
        try {
            Examination exam=new Examination();
            //将examVo拷贝到exam
            BeanUtils.copyProperties(examVo,exam);
            exam.setCreateUserId(examVo.getCreatorId());
            exam.setExamScore(examVo.getTotalScore());
            exam.setPassingScore(examVo.getPassMark());
            exam.setAnswerTime(examVo.getTime());
            exam.setCreateTime(new Date());
            exam.setUpdateTime(new Date());
            exam.setUpdateUserId(examVo.getCreatorId());
            exam.setReleaseYn(examVo.getReleasing());
            //插入试卷
            examinationMapper.insert(exam);

            List<Topic> topics = examVo.getTopics();
            for (Topic topic : topics) {
                Topic topic1=new Topic();
                BeanUtils.copyProperties(topic,topic1);
                topic1.setCreateTime(new Date());
                topic1.setUpdateTime(new Date());
                topic1.setCreateUserId(examVo.getCreatorId());
                topic1.setUpdateUserId(examVo.getCreatorId());
                if(!(topic.getTopicType()==0 || topic.getTopicType()==1)){
                    topic1.setOptionss(null);
                }
                topicMapper.insert(topic1);
                //添加关系数据
                Reation reation = new Reation();
                reation.setExamId(exam.getId());
                reation.setTopicId(topic1.getId());
                reation.setTopicType(topic1.getTopicType());
                reationMapper.insert(reation);
            }
        }catch (Exception e){
            //手动回滚数据
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return Result.error("500","添加异常");
        }
        return Result.success();
    }

    @Override
    public Page<ExamListVo> getExamAll(Integer pageNumber, Integer pageSize, String search) {
        Page<Examination> ExamPage = new Page<>(pageNumber, pageSize);
        Page<ExamListVo> ExamVoPage =new Page<>();
        List<ExamListVo> examList = new ArrayList<ExamListVo>();
        Page<Examination> page = examinationMapper.selectPage(ExamPage, null);
        List<Examination> records = page.getRecords();
        if(StrUtil.isNotBlank(search)){
            List<Examination> examinations = examinationMapper.selectList(null);
            for (Examination exam : examinations) {
                String examName = exam.getExamName();
                if(examName.contains(search)){
                    ExamListVo examListVo = new ExamListVo();
                    BeanUtils.copyProperties(exam,examListVo);
                    Integer subjectId = exam.getSubjectId();
                    Integer createUserId = exam.getCreateUserId();
                    Integer updateUserId = exam.getUpdateUserId();
                    //封装数据
                    Subject subject = subjectMapper.selectById(subjectId);
                    User createUser = userMapper.selectById(createUserId);
                    User updateUser = userMapper.selectById(updateUserId);
                    examListVo.setSubjectName(subject.getSubjectName());
                    examListVo.setCreateUserName(createUser.getName());
                    examListVo.setUpdateUserName(updateUser.getName());
                    examList.add(examListVo);
                }
            }
            ExamVoPage.setRecords(examList);
            ExamVoPage.setTotal(examList.size());
        }else{
            for (Examination exam : records) {
                ExamListVo examListVo = new ExamListVo();
                BeanUtils.copyProperties(exam,examListVo);
                Integer subjectId = exam.getSubjectId();
                Integer createUserId = exam.getCreateUserId();
                Integer updateUserId = exam.getUpdateUserId();
                //封装数据
                Subject subject = subjectMapper.selectById(subjectId);
                User createUser = userMapper.selectById(createUserId);
                User updateUser = userMapper.selectById(updateUserId);
                if(subject!=null){
                    examListVo.setSubjectName(subject.getSubjectName());
                }
                examListVo.setCreateUserName(createUser.getName());
                examListVo.setUpdateUserName(updateUser.getName());
                examList.add(examListVo);
            }
            ExamVoPage.setRecords(examList);
            ExamVoPage.setTotal(page.getTotal());
        }
        return ExamVoPage;
    }

    @Override
    public Result updateReleaseYn(Integer id,Integer yn,Integer userId) {
        Examination examination = examinationMapper.selectById(id);
        try {
            if(examination!=null){
                examination.setReleaseYn(yn);
                examination.setUpdateUserId(userId);
                examination.setUpdateTime(new Date());
                examinationMapper.updateById(examination);
            }
            return Result.success();
        }catch (Exception e){
            //强制手动事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.error("500","试卷状态修改失败");
        }
    }

    @Override
    public Result deleteExam(Integer id, Integer userId) {
        try {
            //删除试卷前  需要删除所有题目
            List<Reation> reations = reationMapper.selectList(Wrappers.<Reation>lambdaQuery().eq(Reation::getExamId, id));
            for (Reation reation : reations) {
                Integer topicId = reation.getTopicId();
                //修改题目 时间/用户id
                Topic topic = topicMapper.selectById(topicId);
                topic.setUpdateUserId(userId);
                topic.setUpdateTime(new Date());
                topicMapper.updateById(topic);
                //删除题目
                topicMapper.deleteById(topicId);
            }
            //删除关系表
            reationMapper.delete(Wrappers.<Reation>lambdaQuery().eq(Reation::getExamId,id));
            //删除试卷
            examinationMapper.deleteById(id);
            return Result.success();
        }catch (Exception e){
            //强制手动事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return Result.error("500","试卷删除失败！");
        }
    }

    @Override
    public Result getExamById(Integer id) {
        ExamVo examVo = new ExamVo();
        List<Topic> topics=new ArrayList<>();
        Examination examination = examinationMapper.selectById(id);
        Integer userId = examination.getCreateUserId();
        User user = userMapper.selectById(userId);
        //根据试卷id查询试题
        if(examination!=null){
            List<Reation> reations = reationMapper.selectList(Wrappers.<Reation>lambdaQuery().eq(Reation::getExamId, id));
            //查出关系列表然后 在查询试题
            for (Reation reation : reations) {
                Integer topicId = reation.getTopicId();
                //根据题目id查询试题
                Topic topic = topicMapper.selectById(topicId);
                topics.add(topic);
            }
        }
        BeanUtils.copyProperties(examination,examVo);
        examVo.setCreatorName(user.getName());
        examVo.setTopics(topics);
        examVo.setExamId(examination.getId());
        examVo.setCreatorId(examination.getCreateUserId());
        examVo.setTime(examination.getAnswerTime());
        examVo.setCreateDate(examination.getCreateTime());
        examVo.setUpdateDate(examination.getUpdateTime());
        examVo.setTotalScore(examination.getExamScore());
        examVo.setPassMark(examination.getPassingScore());
        examVo.setReleasing(examination.getReleaseYn());
        examVo.setSubjectName(subjectMapper.selectById(examination.getSubjectId()).getSubjectName());
        return Result.success(examVo);
    }

    @Override
    public Result updateExam(ExamVo examVo, Integer userId) {
        int flage=0;
        String topicStr="";
        try {
            //修改试卷信息
            Examination exam=new Examination();
            //将examVo拷贝到exam
            BeanUtils.copyProperties(examVo,exam);
            exam.setId(examVo.getExamId());
            exam.setCreateUserId(examVo.getCreatorId());
            exam.setExamScore(examVo.getTotalScore());
            exam.setPassingScore(examVo.getPassMark());
            exam.setAnswerTime(examVo.getTime());
            exam.setCreateTime(examVo.getCreateDate());
            exam.setReleaseYn(examVo.getReleasing());
            //修改 修改用户信息
            exam.setUpdateTime(new Date());
            exam.setUpdateUserId(userId);
            examinationMapper.updateById(exam);
            //需要判断试卷题目中新增 和修改的题目
            List<Topic> topics = examVo.getTopics();
            Integer examId = exam.getId();
            //根据试卷id查询历史题目数据
            List<Reation> reations = reationMapper.selectList(Wrappers.<Reation>lambdaQuery().eq(Reation::getExamId, examId));
            //创建set集合
            HashSet<Integer> set = new HashSet<>();
            for (Topic t : topics) {
                flage++;
                //判断题目是否存在
                Integer id = t.getId();
                if(id==null){
                    Topic topic=new Topic();
                    BeanUtils.copyProperties(t,topic);
                    topic.setCreateTime(new Date());
                    topic.setUpdateTime(new Date());
                    topic.setCreateUserId(userId);
                    topic.setUpdateUserId(userId);
                    if(!(topic.getTopicType()==0 || topic.getTopicType()==1)){
                        topic.setOptionss(null);
                    }
                    topicStr="添加失败,请检查第"+flage+"题！";
                    topicMapper.insert(topic);
                    //再次添加关系
                    Reation reation = new Reation();
                    reation.setExamId(examVo.getExamId());
                    reation.setTopicId(topic.getId());
                    reation.setTopicType(t.getTopicType());
                    reationMapper.insert(reation);
                }else{
                    //修改
                    topicStr="修改失败,请检查第"+flage+"题！";
                    topicMapper.updateById(t);
                    set.add(t.getId());
                }
            }
            //先删除数据
            for (Reation reation : reations) {
                Integer topicId = reation.getTopicId();
                boolean contains = set.contains(topicId);
                if(!contains){
                    System.out.println("删除数据---id:"+topicId);
                    topicMapper.deleteById(topicId);
                    reationMapper.delete(Wrappers.<Reation>lambdaQuery().eq(Reation::getTopicId,topicId));
                }
            }
        }catch (Exception e){
            //强制手动事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            System.out.println("异常了---->"+topicStr);
            return Result.error("500",topicStr);
        }
        return Result.success();
    }

    @Override
    public Page<UserExamListVo> getUserExamAll(Integer pageNumber, Integer pageSize, String search,Integer userId) {
        Page<Examination> examPage = new Page<>(pageNumber, pageSize);
        Page<UserExamListVo> userExamListVoPage =new Page<>();
        List<UserExamListVo> userExamListVoList = new ArrayList<UserExamListVo>();
        QueryWrapper<Examination> queryWrapper = new QueryWrapper<>();
        //判断search条件是否为空
        if(StrUtil.isNotBlank(search)){
            //构造条件 模糊查询
            queryWrapper.like("exam_name",search);
        }
        queryWrapper.eq("release_yn",0);//且为已经发布

        Page<Examination> page = examinationMapper.selectPage(examPage, queryWrapper);

        List<Examination> records = page.getRecords();
        for (Examination record : records) {
            UserExamListVo userExamListvos = new UserExamListVo();
            BeanUtils.copyProperties(record,userExamListvos);
            UserGrade userGrade = userGradeMapper.selectOne(Wrappers.<UserGrade>lambdaQuery().eq(UserGrade::getExamId, record.getId()).eq(UserGrade::getUserId,userId));
            if(userGrade!=null){
                userExamListvos.setUserGradeId(userGrade.getId());
                userExamListvos.setExamHsTime(userGrade.getAnswerTime());
                userExamListvos.setAnswerDate(userGrade.getAnswerDate());
                if(userGrade.getMarkDate()!=null){
                    userExamListvos.setMarkDate(userGrade.getMarkDate());
                }
                if(userGrade.getExamStatus()==2){
                    double grade = userGrade.getGrade();
                    float score= (float) grade;
                    userExamListvos.setGrade(score);//添加分数
                }
                userExamListvos.setExamStatus(userGrade.getExamStatus());
            }else{
                userExamListvos.setExamStatus(0);//未完成
            }
            userExamListvos.setSubjectName(subjectMapper.selectById(record.getSubjectId()).getSubjectName());
            userExamListVoList.add(userExamListvos);
        }
        userExamListVoPage.setTotal(page.getTotal());
        userExamListVoPage.setRecords(userExamListVoList);
        return userExamListVoPage;
    }

    @Override
    public Page<AdminExamListVo> getAdminExamAll(Integer pageNumber, Integer pageSize, String search) {
        Page<UserGrade> userGradePage = new Page<>(pageNumber, pageSize);
        Page<AdminExamListVo> adminExamListVoPage =new Page<>();
        List<AdminExamListVo> adminExamListVoList = new ArrayList<AdminExamListVo>();
        QueryWrapper<UserGrade> queryWrapper = new QueryWrapper<>();
        if(StrUtil.isNotBlank(search)){
            //构造条件 模糊查询
            queryWrapper.like("exam_name",search);
        }
        Page<UserGrade> gradePage = userGradeMapper.selectPage(userGradePage, queryWrapper);
        List<UserGrade> userGrades = gradePage.getRecords();
        for (UserGrade userGrade : userGrades) {
            AdminExamListVo adminExamListVo = new AdminExamListVo();
            Integer examId = userGrade.getExamId();
            Integer userId = userGrade.getUserId();
            Examination examination = examinationMapper.selectById(examId);
            User user = userMapper.selectById(userId);
            Subject subject = subjectMapper.selectById(examination.getSubjectId());
            BeanUtils.copyProperties(userGrade,adminExamListVo);
            adminExamListVo.setUserName(user.getName());
            adminExamListVo.setSubjectName(subject.getSubjectName());
            adminExamListVo.setSubjectId(subject.getId());
            adminExamListVo.setAnswerTime(examination.getAnswerTime());
            adminExamListVo.setExamHsTime(userGrade.getAnswerTime());
            adminExamListVo.setExamScore(examination.getExamScore());
            adminExamListVo.setPassingScore(examination.getPassingScore());
            adminExamListVo.setTopicNum(examination.getTopicNum());
            if(userGrade.getGrade()!=null){
                double garde=userGrade.getGrade();
                adminExamListVo.setGrade((float) garde);
            }
            adminExamListVoList.add(adminExamListVo);
        }
        adminExamListVoPage.setTotal(gradePage.getTotal());
        adminExamListVoPage.setRecords(adminExamListVoList);
        return adminExamListVoPage;
    }
}
