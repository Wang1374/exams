package com.tanmu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@TableName("user_topic")
@Data
public class UserTopic {
      @TableId(type = IdType.AUTO)
      private Integer id;
      private Integer userId;
      private Integer examId;
      private Integer topicId;
      private Integer topicType;
      private String userAnswer;
      private Double userScore;
      private Integer topicStatus;
      @TableLogic(value = "0", delval = "1")
      @TableField(fill = FieldFill.INSERT)
      private Integer deleteYn;

      public String getInfo() {
            return "UserTopic{" +
                    "id=" + id +
                    ", userId=" + userId +
                    ", examId=" + examId +
                    ", topicId=" + topicId +
                    ", topicType=" + topicType +
                    ", userAnswer='" + userAnswer + '\'' +
                    ", userScore=" + userScore +
                    ", topicStatus=" + topicStatus +
                    ", deleteYn=" + deleteYn +
                    '}';
      }
}
