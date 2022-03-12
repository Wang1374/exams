package com.tanmu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanmu.common.Result;
import com.tanmu.entity.User;
import com.tanmu.entity.UserGrade;
import com.tanmu.vo.UserGradeVo;

public interface UserGradeService {
    //插入成绩单
    Result insertUserGrade(UserGradeVo userGradeVo);
    //根据id查询成绩单
    Result getUserGrade(Integer id);
    //根据用户id查询成绩单
    Result getUserGradeByUserId(Integer userId);
    //根据试卷id查询成绩单
    Result getUserGradeByExamId(Integer examId);
    //分页查询所有成绩单
    Page<UserGrade> getUserAll(Integer pageNumber, Integer pageSize, String search);
    //修改成绩单
    Result updateUserGrade(UserGradeVo userGradeVo);
    //删除成绩单
    Result deleteUserGrade(Integer id);
}
