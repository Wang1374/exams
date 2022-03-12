package com.tanmu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanmu.entity.Subject;
import com.tanmu.vo.SubjectVo;

import java.util.List;

public interface SubjectService {
    //查询分类
    List<Subject> queryAll();
    //获取试卷类型
    Page<SubjectVo> getExamSubjectAll(Integer pageNumber, Integer pageSize, String search);
    //修改类别名称
    int updateSubjectName(SubjectVo subjectVo,Integer userId);
    //删除类别
    int deleteSubject(Integer id,Integer userId);
    //新增类别
    int addSubject(String subjectName,Integer userId);
    //根据名称查询
    Subject selectByName(String subjectName);
}
