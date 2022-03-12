package com.tanmu.vo;

import lombok.Data;

@Data
public class MenuVo {
    private Integer listId;
    private String listTitle;
    private String url;
    private String name;
    private Boolean listShow;
    private Integer listPriority;
    private Integer listRole;
}
