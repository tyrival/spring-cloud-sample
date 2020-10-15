package com.acrel.mybatis.role.service;


import com.acrel.common.base.service.BaseService;
import com.acrel.entity.auth.Role;
import com.acrel.entity.auth.User;
import com.acrel.entity.auth.UserRole;

import java.util.List;

public interface UserRoleService extends BaseService<UserRole> {

    int insert(UserRole userRole);

    int deleteByUserId(Integer userId);

    int deleteByRoleId(Integer roleId);

    int insertBatch(List<UserRole> list);

    List<Role> listRoleByUser(Integer userId);

    List<User> listUserByRole(Integer roleId);
}
