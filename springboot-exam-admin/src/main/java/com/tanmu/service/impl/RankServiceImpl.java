package com.tanmu.service.impl;

import com.tanmu.common.Result;
import com.tanmu.mapper.*;
import com.tanmu.service.RankService;
import com.tanmu.vo.RankVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankServiceImpl implements RankService {
    @Autowired
    private RankMapper rankMapper;
    @Override
    public Result userGetRank() {
        List<RankVo> rankVos = rankMapper.GetRank();
        for (RankVo rankVo : rankVos) {
            rankVo.setUserName("");
            if(rankVo.getRanks()==1){
                rankVo.setGood(1);
                rankVo.setAlias("状元");
            }
            if(rankVo.getRanks()==2){
                rankVo.setGood(1);
                rankVo.setAlias("榜眼");
            }
            if(rankVo.getRanks()==3){
                rankVo.setGood(1);
                rankVo.setAlias("探花");
            }
        }
        return Result.success(rankVos);
    }

    @Override
    public Result adminGetRank() {
        List<RankVo> rankVos = rankMapper.GetRank();
        for (RankVo rankVo : rankVos) {
            if(rankVo.getRanks()==1){
                rankVo.setGood(1);
                rankVo.setAlias("状元");
            }
            if(rankVo.getRanks()==2){
                rankVo.setGood(1);
                rankVo.setAlias("榜眼");
            }
            if(rankVo.getRanks()==3){
                rankVo.setGood(1);
                rankVo.setAlias("探花");
            }
        }
        return Result.success(rankVos);
    }
}
