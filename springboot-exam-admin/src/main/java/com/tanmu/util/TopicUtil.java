package com.tanmu.util;

import java.util.HashSet;

public class TopicUtil {
    public static int ifTopic(String userAnswer,String answer){
        String[] userAnswers = userAnswer.split("\\|");
        String[] answers = answer.split("\\|");
        HashSet answersSet =new HashSet();
        for (String s : answers) {
            answersSet.add(s);
        }

        int i=0;
        for (String s : userAnswers) {
            if(answersSet.contains(s)){
                i++;
            }else {
                return 0;//错误
            }
        }

        if(i==answers.length){
            return 2;//正确
        }else{
           return 1;//正确一半
        }
    }
}
