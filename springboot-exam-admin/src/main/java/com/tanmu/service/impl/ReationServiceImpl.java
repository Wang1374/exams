package com.tanmu.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanmu.common.Result;
import com.tanmu.entity.Examination;
import com.tanmu.entity.Reation;
import com.tanmu.entity.Subject;
import com.tanmu.entity.Topic;
import com.tanmu.mapper.*;
import com.tanmu.service.ReationService;
import com.tanmu.vo.ReationVo;
import com.tanmu.vo.SubjectVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ReationServiceImpl implements ReationService {
    @Autowired
    private ReationMapper reationMapper;
    @Autowired
    private SubjectMapper subjectMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ExaminationMapper examinationMapper;
    @Autowired
    private TopicMapper topicMapper;
    @Override
    public Page<ReationVo> getReationAll(Integer pageNumber, Integer pageSize, String search) {
        Page<ReationVo> reationVo=new Page<>();
        List<ReationVo> list=new ArrayList<>();
        Page<Reation> reationPage = reationMapper.selectPage(new Page<>(pageNumber, pageSize), null);
        if(StrUtil.isNotBlank(search)){
            List<Reation> reations = reationMapper.selectList(null);
            for (Reation record : reations) {
                ReationVo vo = new ReationVo();
                BeanUtils.copyProperties(record,vo);
                Integer examId  = record.getExamId();
                Integer topicId = record.getTopicId();
                Examination examination = examinationMapper.selectById(examId);
                Topic topic = topicMapper.selectById(topicId);
                vo.setExamName(examination.getExamName());
                vo.setTopicName(topic.getQuestion());
                if(vo.getTopicName().contains(search)){
                    list.add(vo);
                }
            }
            reationVo.setTotal(list.size());
            reationVo.setRecords(list);
        }else{
            for (Reation record : reationPage.getRecords()) {
                ReationVo vo = new ReationVo();
                BeanUtils.copyProperties(record,vo);
                Integer examId  = record.getExamId();
                Integer topicId = record.getTopicId();
                Examination examination = examinationMapper.selectById(examId);
                Topic topic = topicMapper.selectById(topicId);
                vo.setExamName(examination.getExamName());
                vo.setTopicName(topic.getQuestion());
                list.add(vo);
            }
            reationVo.setTotal(reationPage.getTotal());
            reationVo.setRecords(list);
        }
        return reationVo;
    }
}
