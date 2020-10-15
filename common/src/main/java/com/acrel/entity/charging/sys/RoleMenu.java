package com.acrel.entity.charging.sys;

import com.acrel.entity.base.Base;
import lombok.Data;

@Data
public class RoleMenu extends Base {

    /**
     * 菜单ID
     */
    private Integer menuId;

    /**
     * 角色ID
     */
    private Integer roleId;

}
