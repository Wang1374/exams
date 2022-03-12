package com.tanmu.mapper;
import com.tanmu.vo.RankVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RankMapper {
    List<RankVo> GetRank();
}
