package com.tanmu.vo;
import com.tanmu.entity.Menu;
import lombok.Data;
import java.util.List;
@Data
public class MenuDataVo {
    private Integer menuId;
    private String menuTitle;
    private String icon;
    private Boolean menuShow;
    private Integer menuPriority;
    private Integer menuRole;
    private List<MenuVo> menu;
}
