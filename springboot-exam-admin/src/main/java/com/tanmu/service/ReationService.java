package com.tanmu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanmu.common.Result;
import com.tanmu.vo.ReationVo;
import com.tanmu.vo.SubjectVo;

public interface ReationService {
    //获取关系列表
    Page<ReationVo> getReationAll(Integer pageNumber, Integer pageSize, String search);
}
