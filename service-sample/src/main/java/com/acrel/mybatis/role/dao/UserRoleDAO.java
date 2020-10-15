package com.acrel.mybatis.role.dao;

import com.acrel.common.base.dao.BaseDAO;
import com.acrel.entity.auth.Role;
import com.acrel.entity.auth.User;
import com.acrel.entity.auth.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface UserRoleDAO extends BaseDAO<UserRole> {

    int deleteByUserId(Integer userId);

    int deleteByRoleId(Integer roleId);

    int insertBatch(List<UserRole> list);

    List<Role> listRoleByUser(Integer userId);

    List<User> listUserByRole(Integer roleId);
}
