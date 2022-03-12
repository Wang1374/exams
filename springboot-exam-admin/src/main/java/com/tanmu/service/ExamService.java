package com.tanmu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanmu.common.Result;
import com.tanmu.entity.Examination;
import com.tanmu.entity.User;
import com.tanmu.vo.*;

public interface ExamService {
    //添加试卷
    Result addExam(ExamVo examVo);
    //获取所有试卷信息 分页形式
    Page<ExamListVo> getExamAll(Integer pageNumber, Integer pageSize, String search);
    //修改试卷是否发布
    Result updateReleaseYn(Integer id,Integer yn,Integer userId);
    //删除试卷
    Result deleteExam(Integer id,Integer userId);
    //根据id查询试卷信息
    Result getExamById(Integer id);
    //修改试卷
    Result updateExam(ExamVo examVo,Integer userId);
    //获取用户试卷信息 分页
    Page<UserExamListVo> getUserExamAll(Integer pageNumber, Integer pageSize, String search,Integer userId);
    //获取用户试卷信息 分页
    Page<AdminExamListVo> getAdminExamAll(Integer pageNumber, Integer pageSize, String search);
}
