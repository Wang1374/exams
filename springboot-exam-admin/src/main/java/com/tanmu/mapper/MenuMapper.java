package com.tanmu.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tanmu.entity.Menu;
import com.tanmu.entity.Reation;
import com.tanmu.vo.MenuDataVo;
import com.tanmu.vo.MenuVo;
import com.tanmu.vo.RankVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    List<MenuDataVo> getMenu(@Param("role") Integer role);
    List<MenuVo> getRouter(@Param("role") Integer role);
    List<MenuDataVo> getMenuAll(@Param("search") String search);
}
