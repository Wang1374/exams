package com.tanmu.controller;

import com.tanmu.common.Result;
import com.tanmu.entity.User;
import com.tanmu.service.UserService;
import com.tanmu.util.MultipartFileToFileUtil;
import com.tanmu.util.TokenUtil;
import com.tanmu.util.UploadQiniuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    private UserService userService;
    //上传图片文件
    @PostMapping("/uploadImage")
    public Result uploadImage(MultipartFile file, HttpServletRequest request) throws Exception {
        String token = request.getHeader("token");
        String userPhone = TokenUtil.getUserPhone(token);
        User user = userService.getUserByPhone(userPhone);
        if(user==null){
            return Result.error("1007","token不存在！");
        }
        if(file ==null){
            return Result.error("500","文件为空！");
        }
        //MultipartFile转file
        File toFile = MultipartFileToFileUtil.multipartFileToFile(file);
        //上传到七牛云 返回url地址
        String imgUrl = UploadQiniuUtil.uploadImage(toFile);
        if(imgUrl==null){
            return Result.error("500","上传失败,请检查文件是否正确！");
        }
        return Result.success(imgUrl);
    }
}
