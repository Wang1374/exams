package com.tanmu.util;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.teaopenapi.models.Config;

public class ShortMsgUtil {
    public static String SendMessage(String phone,String code) throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId("LTAI4GGqCSmPbXcLPmKAeCcM")
                // 您的AccessKey Secret
                .setAccessKeySecret("mKC7q1WJgVwrWUYzAx8PWtR2rwYJf2");
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        Client client = new Client(config);
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(phone)
                .setSignName("檀木考试系统")
                .setTemplateCode("SMS_228846132")
                .setTemplateParam("{\"code\":\""+code+"\"}");
        // 复制代码运行请自行打印 API 的返回值
        SendSmsResponse sms = client.sendSms(sendSmsRequest);
        SendSmsResponseBody body = sms.getBody();
        String msg="code = " + body.getCode()+"\n"
                +"Message = " + body.getMessage()+"\n"
                +"BizId = " + body.getBizId()+"\n"
                +"RequestId = " + body.getRequestId();
        return msg;
    }
    public static String forgetPasswordSendMessage(String phone,String code) throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId("LTAI4GGqCSmPbXcLPmKAeCcM")
                // 您的AccessKey Secret
                .setAccessKeySecret("mKC7q1WJgVwrWUYzAx8PWtR2rwYJf2");
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        Client client = new Client(config);
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(phone)
                .setSignName("檀木考试系统")
                .setTemplateCode("SMS_228851333")
                .setTemplateParam("{\"code\":\""+code+"\"}");
        // 复制代码运行请自行打印 API 的返回值
        SendSmsResponse sms = client.sendSms(sendSmsRequest);
        SendSmsResponseBody body = sms.getBody();
        String msg="code = " + body.getCode()+"\n"
                +"Message = " + body.getMessage()+"\n"
                +"BizId = " + body.getBizId()+"\n"
                +"RequestId = " + body.getRequestId();
        return msg;
    }
}
