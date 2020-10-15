package com.acrel.mybatis.user.dao;

import com.acrel.common.base.dao.BaseDAO;
import com.acrel.entity.auth.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: ZhouChenyu
 */
@Mapper
@Component
public interface UserDAO extends BaseDAO<User> {

    User getPasswordByAccount(String account);

    User getByAccount(String account);

}
