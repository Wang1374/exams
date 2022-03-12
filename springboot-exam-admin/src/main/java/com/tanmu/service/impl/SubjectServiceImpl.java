package com.tanmu.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanmu.entity.Examination;
import com.tanmu.entity.Subject;
import com.tanmu.entity.User;
import com.tanmu.mapper.ExaminationMapper;
import com.tanmu.mapper.SubjectMapper;
import com.tanmu.mapper.UserMapper;
import com.tanmu.service.SubjectService;
import com.tanmu.vo.SubjectVo;
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
public class SubjectServiceImpl implements SubjectService {
    @Autowired
    private SubjectMapper subjectMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ExaminationMapper examinationMapper;
    @Override
    public List<Subject> queryAll() {
        List<Subject> subjects = subjectMapper.selectList(null);
        return subjects;
    }

    @Override
    public Page<SubjectVo> getExamSubjectAll(Integer pageNumber, Integer pageSize, String search) {
        Page<SubjectVo> subjectVoPage=new Page<>();
        List<SubjectVo> list=new ArrayList<>();
        Page<Subject> SubjectPage = new Page<>(pageNumber, pageSize);
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        //判断search条件是否为空
        if(StrUtil.isNotBlank(search)){
            //构造条件 模糊查询
            queryWrapper.like("subject_name",search);
        }
        Page<Subject> subjectPage = subjectMapper.selectPage(SubjectPage, queryWrapper);
        List<Subject> records = subjectPage.getRecords();
        for (Subject record : records) {
            SubjectVo subjectVo=new SubjectVo();
            BeanUtils.copyProperties(record,subjectVo);
            Integer createUserId = record.getCreateUserId();
            Integer updateUserId = record.getUpdateUserId();
            User createUser = userMapper.selectById(createUserId);
            User updateUser = userMapper.selectById(updateUserId);
            subjectVo.setCreateUserName(createUser.getName());
            subjectVo.setUpdateUserName(updateUser.getName());
            list.add(subjectVo);
        }
        subjectVoPage.setTotal(SubjectPage.getTotal());
        subjectVoPage.setRecords(list);
        return subjectVoPage;
    }

    @Override
    public int updateSubjectName(SubjectVo subjectVo,Integer userId) {
        try {
            Subject subject = new Subject();
            BeanUtils.copyProperties(subjectVo,subject);
            subject.setUpdateUserId(userId);
            subject.setUpdateTime(new Date());
            int i = subjectMapper.updateById(subject);
            return i;
        }catch (Exception e){
            //强制手动事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int deleteSubject(Integer id,Integer userId) {
        //删除前判断试卷列表是否使用
        List<Examination> list = examinationMapper.selectList(Wrappers.<Examination>lambdaQuery().eq(Examination::getSubjectId, id));
        if(list.size()>0){
            return -1;
        }
        try {
            Subject subject = subjectMapper.selectById(id);
            subject.setUpdateUserId(userId);
            subject.setUpdateTime(new Date());
            subjectMapper.updateById(subject);
            int i = subjectMapper.deleteById(id);
            return i;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int addSubject(String subjectName,Integer userId) {
        try {
            Subject subject = new Subject();
            subject.setCreateTime(new Date());
            subject.setCreateUserId(userId);
            subject.setUpdateTime(new Date());
            subject.setUpdateUserId(userId);
            subject.setSubjectName(subjectName);
            int insert = subjectMapper.insert(subject);
            return 0;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Subject selectByName(String subjectName) {
        Subject subject = subjectMapper.selectOne(Wrappers.<Subject>lambdaQuery().eq(Subject::getSubjectName, subjectName));
        return subject;
    }
}
