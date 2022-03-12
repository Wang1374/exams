package com.tanmu.controller;

import com.tanmu.common.Result;
import com.tanmu.entity.User;
import com.tanmu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //返回json格式
@RequestMapping("/login") //user接口
public class LoginController {
    @Autowired
    private UserService userService;

    //用户登录
    @PostMapping("/Login")
    public Result<?> loginAdmin(@RequestBody User user){
        return userService.login(user);
    }
}
