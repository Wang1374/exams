package com.tanmu.service;

import com.tanmu.common.Result;
import com.tanmu.entity.Menu;
import org.apache.ibatis.annotations.Param;

public interface MenuService {
    //根据权限获取菜单
    Result getMenu(Integer role);
    Result getRouter(Integer role);
    //获取所有菜单
    Result getMenuAll(Integer role,String search);
    //添加菜单/目录
    Result addMenu(Menu menu);
    //获取菜单对象
    Menu getMenuByTitle(String title,Integer leave,Integer role);
    //修改菜单
    Result updateMenu(Menu menu);
    //删除菜单
    Result deleteMenu(Integer id);
}
