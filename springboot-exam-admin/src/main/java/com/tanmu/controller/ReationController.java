package com.tanmu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanmu.common.Result;
import com.tanmu.service.ReationService;
import com.tanmu.vo.ReationVo;
import com.tanmu.vo.SubjectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController //返回json格式
@RequestMapping("/reation") //接口
public class ReationController {
     @Autowired
     private ReationService reationService;
     //获取关系列表
     @GetMapping("/getReationAll")
     public Result<?> getReationAll(@RequestParam(defaultValue ="1") Integer pageNumber, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "") String search){
          //分页查询
          Page<ReationVo> reationAll = reationService.getReationAll(pageNumber, pageSize, search);
          return Result.success(reationAll);
     }

}
