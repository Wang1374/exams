package com.tanmu.filter;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//配置拦截器
@Configuration
public class GlobalConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加拦截器
        InterceptorRegistration interceptor = registry.addInterceptor(new JwtInterceptor());
        //放行接口
        interceptor.excludePathPatterns(
                "/login/Login",
                "/captcha/get",
                "/captcha/check",
                "/captcha/sendMsg",
                "/captcha/forgetPasswordSendMsg",
                "/user/userResetPassword",
                "/user/register");
    }
}
