package com.acrel.mybatis.role.service;

import com.acrel.common.base.service.BaseService;
import com.acrel.entity.auth.Role;

import java.util.List;

public interface RoleService extends BaseService<Role> {

    /**
     * 列出角色及相关的用户信息
     * @param role
     * @return
     */
    List<Role> listCascade(Role role);
}
