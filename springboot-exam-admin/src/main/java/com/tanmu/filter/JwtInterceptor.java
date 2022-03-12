package com.tanmu.filter;

import com.anji.captcha.util.JsonUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tanmu.common.Result;
import com.tanmu.entity.User;
import com.tanmu.mapper.UserMapper;
import com.tanmu.service.UserService;
import com.tanmu.util.RsaUtil;
import com.tanmu.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class JwtInterceptor extends HandlerInterceptorAdapter {
    private String privateKey="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCgqq00Lz61hY0BQc30MQ2yTNGKrgBjyB5jwmsVdbDQDqUUJsuJ78pygPvxknxAWlBaXHl8fLm24H7cZ90Hzb0JgrwWWOYSWyq8iPmrg1AacGtJT1RRm7NEDzlixDl8GbAllOL2ViD5yEgWRfAoiBbbY/Ja7iwiPAp0s/6rQQkFBFgnhH9hOtXIbweZ7UMS3/iIP7zzmaOBja/vJjPk2aEJXixkLSxkUycE+J0uznBLj0lnj9i4q4zC3BFPav3+jexXpU01Rm1VgHPx+XlRFC0njLEjeG/24kDO6U6dczLxRjAFUbRvSwTgTEX7KVZea5TApu6ln1qyOwck9lMZRTC1AgMBAAECggEAHqHL2goz64nl41p3eNotYVQyEz2vh1atLJnxrqq8pj5UjO4EmmzkbUZYQquKEv7lGQVOiESDZ/GPXlhTFNzN8NVDJQOYAeHt2PpIl8G2cQQEAHsmeVS1LgG1EDaXOyjuo4uqZgrvyI+95rk425Fuwi9hEqMcOP63g56ZrG7+2Dy5d+qA2QNI6auZFCkfJGSmPWzn6ZBLt7aOG535+AW+M76UlyLHeNLX9NzG44A78dFY6CvcnpKfEKxwROMnPdmQ1zEKPszNVJPrcA07FGKOvQu6g2uybsfMM6HcD5tVQBgooTKe8iX/afG6DjumbZXQIMzzYZPobEf54n/bqQi8AQKBgQDMZPzl8+fI8ZDhVgir62eoy5Jk9fyR8Q+iqjpkMVRpgbvuInrnlslP1lPbwlvljQ400RkxkmnIjLO7r+XM1EfC06EuRgzuF3NredGQ3TxHlyyeBk9WEv7XDJ9SCKZ5eMYDL+B2KQCAD9mMQYU+/6t1xB43ZHLncL1jVF9enq3FAQKBgQDJO1lh/vOonKu7sMiY7RU3s+z5HucQ8xizTVHPX5tMcDMg/BximdHcbuZno38nUFqMOmouDiho1AI/gnrXxmsmUxJ+nhxhcdxFVyK+B4JbIiOkva5mfFtUtXEQXQCVQmIK6yJcWyD1DL31SQC+8DD0lXTRbXvSS/O6/2kUIqXntQKBgQClFRu9jpWykoj1tssiL+t1RZZjvNnV9eEu/FZU/hKzdHkZCPhDfGHz/aOVZxFaAedreUbj5uFRfXLdMGBmYz3ieZNBEIr48i/iqm08l7NMDymWKHqDhqwVfLhhRWNP0N/9Svn79XnV9Pc8dN7t1wnhABl3m8XCcChGxaqRfY/UAQKBgFk+ifiNAnDP8tXNpFk2JAfNMxaU3vNEbLrwWoz2oMA4rmQ9izUrdMgnIuXpu7WqzE2OhpKgUsmS5OBXtLVHWTPo+bXP11RcE81oxI60c9qI/HfOa68mDbc2tMsGBAEuLJ8dAuMkwywKl6BV3Q/RJFUEh4dt39E3QAO5zFdSunNVAoGAHUHnNzJT140VZdTY+fjaiWEALXeIghpLYNrf8FGNnkMzzAks9QzPRchsZyMf37mjZQfbzQqg9Q+KUQZ69t59ujPg4K1T73clRjWuetr2boQz4Yrjms0ixhuUNkLiLk4wcPctZDPALh1/aJtK1NzD7wIqrQ0owBe1eWzvgYl5eDw=";
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        String phone = request.getHeader("phone");
        //解密
        if(phone!=null){
            phone=RsaUtil.decrypt(phone,privateKey);
        }

        if (token == null) {
            return false;
        }

        if(phone==null){
            return false;
        }

        //判断是否同一个手机号
        if(!TokenUtil.isPhone(token,phone)){
            return false;
        }

        //验证token
        return TokenUtil.checkToken(token);
    }

}
