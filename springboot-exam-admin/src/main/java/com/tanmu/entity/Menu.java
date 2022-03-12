package com.tanmu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("menu")
public class Menu {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String title;
    private Integer level;
    private String icon;
    private String url;
    private String name;
    private String shows;
    private String priority;
    private Integer parentId;
    private Integer role;
    //自动填充 默认0   @TableLogic定义删除逻辑为1
    @TableLogic(value = "0", delval = "1")
    @TableField(fill = FieldFill.INSERT)
    private Integer deleteYn;
}
