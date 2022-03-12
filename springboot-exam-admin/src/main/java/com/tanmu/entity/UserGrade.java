package com.tanmu.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.bouncycastle.cms.PasswordRecipientId;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@TableName("user_grade")
@Data
public class UserGrade {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer examId;
    private String examName;
    private Double grade;//得分
    private Double gradeAuto;//自动评分
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8") //返回日期格式
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//接收日期格式
    private Date answerDate;//答题日期
    private Integer answerTime;//答题时长
    private Integer markStatus;//批改状态 0:待批改 1:批改完成
    private Integer examStatus;//试卷状态 0:未完成  1:批改中 2:已完成
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8") //返回日期格式
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//接收日期格式
    private Date markDate;     //批改日期
    //自动填充 默认0   @TableLogic定义删除逻辑为1
    @TableLogic(value = "0", delval = "1")
    @TableField(fill = FieldFill.INSERT)
    private Integer deleteYn;

    public String getInfo() {
        return "UserGrade{" +
                "id=" + id +
                ", userId=" + userId +
                ", examId=" + examId +
                ", examName='" + examName + '\'' +
                ", grade=" + grade +
                ", gradeAuto=" + gradeAuto +
                ", answerDate=" + answerDate +
                ", answerTime=" + answerTime +
                ", markStatus=" + markStatus +
                ", examStatus=" + examStatus +
                ", markDate=" + markDate +
                ", deleteYn=" + deleteYn +
                '}';
    }
}
