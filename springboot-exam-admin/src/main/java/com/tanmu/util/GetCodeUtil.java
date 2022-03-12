package com.tanmu.util;

import java.util.Random;

public class GetCodeUtil {
    public static String getCode(int count){
        String code ="";
        for (int i = 1; i <=count ; i++) {
            code+= new Random().nextInt(10) + "";
        }
        return code;
    }
}
