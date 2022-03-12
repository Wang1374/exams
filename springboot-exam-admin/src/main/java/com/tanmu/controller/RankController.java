package com.tanmu.controller;

import com.tanmu.common.Result;
import com.tanmu.entity.User;
import com.tanmu.service.RankService;
import com.tanmu.service.UserService;
import com.tanmu.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@RestController //返回json格式
@RequestMapping("/rank")
public class RankController {
    @Autowired
    private RankService rankService;
    @Autowired
    private UserService userService;
    @GetMapping("/userGetRank")
    //普通用户获取排名 不带真实姓名 只显示昵称
    public Result<?> userGetRank(HttpServletRequest request) throws UnsupportedEncodingException {
        String token = request.getHeader("token");
        String userPhone = TokenUtil.getUserPhone(token);
        User user = userService.getUserByPhone(userPhone);
        if(user==null){
            return Result.error("1007","token不存在！");
        }
        Result result = rankService.userGetRank();
        return result;
    }

    @GetMapping("/adminGetRank")
    //管理员获取排名 带真实姓名 显示昵称
    public Result<?> adminGetRank(HttpServletRequest request) throws UnsupportedEncodingException {
        String token = request.getHeader("token");
        String userPhone = TokenUtil.getUserPhone(token);
        User user = userService.getUserByPhone(userPhone);
        if(user==null){
            return Result.error("1007","token不存在！");
        }
        if(user.getRole()>1){
            return Result.error("400","没有权限访问！");
        }
        Result result = rankService.adminGetRank();
        return result;
     }
}
