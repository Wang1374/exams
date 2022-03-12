package com.tanmu.util;


import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import java.io.File;

//上传文件到七牛云
public class UploadQiniuUtil {
    public static String uploadImage(File file) throws Exception {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.regionAs0());
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "mnltCODer6aKUHLrBrNlBvqfUdyud1E9PaG-3420";
        String secretKey = "lROUVZ_tsXaricycM0aTHuUVoMDSfj3KRmYdAw2g";
        String bucket = "tanmuexam";
        String key=null;
        String http=null;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        key=file.getName();
        http="http://qnyun.tanmu.icu/";
        //http://r13zf78ql.bkt.clouddn.com/%E5%9B%BE%E7%89%871.png
        try {
            Response response = uploadManager.put(file.getPath(), key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
            http+=putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
        return http;
    }
}
