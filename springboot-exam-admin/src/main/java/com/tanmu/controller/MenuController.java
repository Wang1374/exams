package com.tanmu.controller;

import com.tanmu.common.Result;
import com.tanmu.entity.Menu;
import com.tanmu.entity.User;
import com.tanmu.service.MenuService;
import com.tanmu.service.RankService;
import com.tanmu.service.UserService;
import com.tanmu.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@RestController //返回json格式
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;
    @Autowired
    private UserService userService;
    @GetMapping("/getMenu")
    public Result<?> getMenu(HttpServletRequest request) throws UnsupportedEncodingException {
        String token = request.getHeader("token");
        String userPhone = TokenUtil.getUserPhone(token);
        User user = userService.getUserByPhone(userPhone);
        if(user==null){
            return Result.error("1007","token不存在！");
        }
        Result menu = menuService.getMenu(user.getRole());
        return menu;
    }

    @GetMapping("/getRouter")
    public Result<?> getRouter(HttpServletRequest request) throws UnsupportedEncodingException {
        String token = request.getHeader("token");
        String userPhone = TokenUtil.getUserPhone(token);
        User user = userService.getUserByPhone(userPhone);
        if(user==null){
            return Result.error("1007","token不存在！");
        }
        Result Router = menuService.getRouter(user.getRole());
        return Router;
    }

    //获取所有菜单
    @GetMapping("/getMenuAll/{search}")
    public Result<?> getMenuAll(@PathVariable String search,HttpServletRequest request) throws UnsupportedEncodingException {
        String token = request.getHeader("token");
        String userPhone = TokenUtil.getUserPhone(token);
        User user = userService.getUserByPhone(userPhone);
        if(user==null){
            return Result.error("1007","token不存在！");
        }
        Result menuAll = menuService.getMenuAll(user.getRole(),search);
        return menuAll;
    }
    //新增菜单
    @PostMapping("/add")  //?表示任意一种类型
    public Result<?> add(@RequestBody Menu menu,HttpServletRequest request) throws UnsupportedEncodingException {
        String token = request.getHeader("token");
        String userPhone = TokenUtil.getUserPhone(token);
        User user = userService.getUserByPhone(userPhone);
        if(user==null){
            return Result.error("1007","token不存在！");
        }
        //添加之前先判断菜单是否存在 根据类别 和菜单名称 和权限 判断
        Menu menuByTitle = menuService.getMenuByTitle(menu.getTitle(), menu.getLevel(), menu.getRole());
        if(menuByTitle==null){
            //允许添加
            Result result = menuService.addMenu(menu);
            return result;
        }else{
            return Result.error("1008","菜单或者目录已经存在！");
        }
    }
    //修改菜单
    @PutMapping("/update")  //?表示任意一种类型
    public Result<?> update(@RequestBody Menu menu,HttpServletRequest request) throws UnsupportedEncodingException {
        String token = request.getHeader("token");
        String userPhone = TokenUtil.getUserPhone(token);
        User user = userService.getUserByPhone(userPhone);
        if(user==null){
            return Result.error("1007","token不存在！");
        }
        Result result = menuService.updateMenu(menu);
        return result;
    }
    //删除菜单 根据id
    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Integer id,HttpServletRequest request) throws UnsupportedEncodingException {
        String token = request.getHeader("token");
        String userPhone = TokenUtil.getUserPhone(token);
        User user = userService.getUserByPhone(userPhone);
        if(user==null){
            return Result.error("1007","token不存在！");
        }
        Result result = menuService.deleteMenu(id);
        return result;
    }
}
