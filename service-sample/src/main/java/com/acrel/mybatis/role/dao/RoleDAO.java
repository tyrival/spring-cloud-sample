package com.acrel.mybatis.role.dao;

import com.acrel.common.base.dao.BaseDAO;
import com.acrel.entity.auth.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface RoleDAO extends BaseDAO<Role> {

    List<Role> listCascade(Role role);
}
