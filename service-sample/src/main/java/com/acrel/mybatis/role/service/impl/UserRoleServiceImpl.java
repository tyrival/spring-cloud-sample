package com.acrel.mybatis.role.service.impl;

import com.acrel.mybatis.role.dao.UserRoleDAO;
import com.acrel.mybatis.role.service.UserRoleService;
import com.acrel.common.base.dao.BaseDAO;
import com.acrel.common.base.service.impl.BaseServiceImpl;
import com.acrel.entity.auth.Role;
import com.acrel.entity.auth.User;
import com.acrel.entity.auth.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl extends BaseServiceImpl<UserRole> implements UserRoleService {

    @Autowired
    private UserRoleDAO userRoleDAO;

    @Override
    public BaseDAO<UserRole> getDAO() {
        return userRoleDAO;
    }

    @Override
    public int deleteByUserId(Integer userId) {
        return this.userRoleDAO.deleteByUserId(userId);
    }

    @Override
    public int deleteByRoleId(Integer roleId) {
        return this.userRoleDAO.deleteByRoleId(roleId);
    }

    @Override
    public int insertBatch(List<UserRole> list) {
        return this.userRoleDAO.insertBatch(list);
    }

    @Override
    public int insert(UserRole userRole) {
        return this.userRoleDAO.insert(userRole);
    }

    @Override
    public List<Role> listRoleByUser(Integer userId) {
        return this.userRoleDAO.listRoleByUser(userId);
    }

    @Override
    public List<User> listUserByRole(Integer roleId) {
        return this.userRoleDAO.listUserByRole(roleId);
    }
}
