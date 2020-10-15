package com.acrel.entity.charging.sys;

import com.acrel.entity.base.TreeNode;
import com.acrel.enums.charging.MenuTypeEnum;
import lombok.Data;

@Data
public class Menu extends TreeNode {

    private String menu;

    private String url;

    private Integer order;

    private MenuTypeEnum type;

}
