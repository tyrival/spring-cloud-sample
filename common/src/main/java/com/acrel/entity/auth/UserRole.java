package com.acrel.entity.auth;

import com.acrel.entity.base.Base;
import lombok.Data;

@Data
public class UserRole extends Base {

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 角色ID
     */
    private Integer roleId;

    public UserRole() {
    }

    public UserRole(Integer userId, Integer roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}