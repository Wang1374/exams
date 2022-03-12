package com.tanmu.util;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tanmu.entity.User;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtil {
    private static final Log log = LogFactory.get();
    private static final long EXPIRE_TIME= 24*60*60*1000;//token到期时间2小时  // 2*60*60*1000
    private static final String TOKEN_SECRET="tanmudashuyyds";  //密钥盐

    //生成token令牌
    public static String getToken(User user){
        String token=null;
        try {
            log.info("========================Token验证================================");
            Date expireAt=new Date(System.currentTimeMillis()+EXPIRE_TIME); //当前时间+设定时间=到期时间
            token = JWT.create()
                    .withIssuer("tanmu")//发行人
                    .withClaim("name",user.getName())//存放数据
                    .withClaim("phone",user.getPhone())//存放数据
                    .withExpiresAt(expireAt)//过期时间
                    .sign(Algorithm.HMAC256(TOKEN_SECRET)); //加密盐
            log.info("获取token:"+token);
            log.info("获取时间:"+new Date().toLocaleString());
            log.info("=================================================================");
        } catch (IllegalArgumentException | JWTCreationException | UnsupportedEncodingException je) {
             je.printStackTrace();
        }
        return token;
    }

    //验证token
    public static Boolean checkToken(String token){
        try {
            JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("tanmu").build();//创建token验证器
            DecodedJWT decodedJWT=jwtVerifier.verify(token);
            log.info("========================Token验证================================");
           log.info("认证通过");
           log.info("认证时间:"+new Date().toLocaleString());
           log.info("name:" + decodedJWT.getClaim("name").asString());
           log.info("phone:" + decodedJWT.getClaim("phone").asString());
           log.info("过期时间:" + decodedJWT.getExpiresAt().toLocaleString());
           log.info("=================================================================");
        } catch (IllegalArgumentException | JWTVerificationException | UnsupportedEncodingException e) {
            //抛出错误即为验证不通过
           log.info("========================Token验证================================");
           log.info("认证失败");
           log.info("认证时间:"+new Date().toLocaleString());
           log.info("=================================================================");
            return false;
        }
        return true;
    }

    //判断是否同一个手机号
  public static Boolean isPhone(String token,String phone){
      try {
          JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("tanmu").build();//创建token验证器
          DecodedJWT decodedJWT=jwtVerifier.verify(token);
          String phone1 = decodedJWT.getClaim("phone").asString();
          boolean equals = phone.equals(phone1);
          if(equals){
              log.info("========================Token验证================================");
              log.info("手机号一致");
              log.info("=================================================================");
              return true;
          }else{
              log.info("========================Token验证================================");
              log.info("手机号不一致");
              log.info("=================================================================");
              return false;
          }
      } catch (IllegalArgumentException | JWTVerificationException | UnsupportedEncodingException e) {
          //抛出错误即为验证不通过
          log.info("========================Token验证================================");
          log.info("token过期");
          log.info("时间:"+new Date().toLocaleString());
          log.info("=================================================================");
          return false;
      }
  }

    //根据token获取用户信息
    public static String getUserPhone(String token) throws UnsupportedEncodingException {
        HashMap<String, String> map = new HashMap<>();
        JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("tanmu").build();//创建token验证器
        DecodedJWT decodedJWT=jwtVerifier.verify(token);
        String phone = decodedJWT.getClaim("phone").asString();
         return phone;
    }
}

