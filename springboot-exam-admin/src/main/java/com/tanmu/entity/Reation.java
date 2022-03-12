package com.tanmu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@TableName("reation")
@Data
public class Reation {
      @TableId(type = IdType.AUTO)
      private Integer id;
      private Integer examId;
      private Integer topicId;
      private Integer topicType;
      //自动填充 默认0   @TableLogic定义删除逻辑为1
      @TableLogic(value = "0", delval = "1")
      @TableField(fill = FieldFill.INSERT)
      private Integer deleteYn;
}

