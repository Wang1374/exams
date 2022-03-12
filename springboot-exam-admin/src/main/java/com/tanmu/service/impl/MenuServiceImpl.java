package com.tanmu.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tanmu.common.Result;
import com.tanmu.entity.Menu;
import com.tanmu.mapper.MenuMapper;
import com.tanmu.service.MenuService;
import com.tanmu.vo.MenuDataVo;
import com.tanmu.vo.MenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
@Service
@Transactional //事务注解
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuMapper menuMapper;
    @Override
    public Result getMenu(Integer role) {
        if(role!=null){
            List<MenuDataVo> menu = menuMapper.getMenu(role);
            return Result.success(menu);
        }else{
            return Result.error("500","角色不存在！");
        }

    }

    @Override
    public Result getRouter(Integer role) {
        if(role!=null){
            List<MenuVo> menu = menuMapper.getRouter(role);
            return Result.success(menu);
        }else{
            return Result.error("500","角色不存在！");
        }
    }

    @Override
    public Result getMenuAll(Integer role,String search) {
        if(role!=null){
            if(role==0){
                //管理员才能管理菜单
                if(!search.equals("null")){
                   search="%"+search+"%";
                }else{
                    search=null;
                }
                List<MenuDataVo> menuAll = menuMapper.getMenuAll(search);
                return Result.success(menuAll);
            }else{
                return Result.error("500","角色权限不够！");
            }
        }else{
            return Result.error("500","角色不存在！");
        }
    }

    @Override
    public Result addMenu(Menu menu) {
        try {
           menuMapper.insert(menu);
            return Result.success();
        }catch (Exception e){
            //强制手动事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return Result.error("500","添加失败！");
        }
    }

    @Override
    public Menu getMenuByTitle(String title, Integer leave, Integer role) {
        Menu menu = menuMapper.selectOne(Wrappers.<Menu>lambdaQuery().eq(Menu::getTitle, title).eq(Menu::getRole, role).eq(Menu::getLevel, leave));
        return menu;
    }

    @Override
    public Result updateMenu(Menu menu) {
        try {
            menuMapper.updateById(menu);
            return Result.success();
        }catch (Exception e){
            //强制手动事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return Result.error("500","修改失败！");
        }
    }

    @Override
    public Result deleteMenu(Integer id) {
        try {
            menuMapper.deleteById(id);
            return Result.success();
        }catch (Exception e){
            //强制手动事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return Result.error("500","删除失败！");
        }
    }
}
