package com.tanmu.service;

import com.tanmu.common.Result;
import com.tanmu.vo.SubmitExamVo;

public interface MarkTestPaperService {
    //获取试卷信息
    Result getUserExamById(Integer user_grade);
    //保存试卷信息
    Result saveExam(SubmitExamVo submitExamVo);
}
