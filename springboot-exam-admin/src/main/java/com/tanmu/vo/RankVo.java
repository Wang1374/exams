package com.tanmu.vo;

import lombok.Data;

@Data
public class RankVo {
    private Integer ranks;//名次
    private Integer userId;//用户id
    private Integer good;//名字是否颜色加重 1加重 0不加重
    private Integer examNumber;//试卷数量
    private Integer topicNumber;//题目数量
    private String accuracy;//正确率
    private String userName;//姓名
    private String nickName;//昵称
    private Double score;//用户总分数
    private Double examScore;//试卷总分数
    private String alias;//标识 1状元 2榜眼  3探花
}
