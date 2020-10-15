package com.acrel.mybatis.role.service.impl;

import com.acrel.mybatis.role.dao.RoleDAO;
import com.acrel.mybatis.role.service.RoleService;
import com.acrel.common.base.dao.BaseDAO;
import com.acrel.common.base.service.impl.BaseServiceImpl;
import com.acrel.entity.auth.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {

    @Autowired
    private RoleDAO roleDAO;

    @Override
    public BaseDAO<Role> getDAO() {
        return roleDAO;
    }

    @Override
    public List<Role> listCascade(Role role) {
        return this.roleDAO.listCascade(role);
    }
}
