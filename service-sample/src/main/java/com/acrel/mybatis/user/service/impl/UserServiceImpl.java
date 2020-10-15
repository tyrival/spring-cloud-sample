package com.acrel.mybatis.user.service.impl;

import com.acrel.mybatis.user.dao.UserDAO;
import com.acrel.mybatis.user.service.UserService;
import com.acrel.common.base.dao.BaseDAO;
import com.acrel.common.base.service.impl.BaseServiceImpl;
import com.acrel.entity.auth.User;
import com.acrel.entity.base.Token;
import com.acrel.enums.base.CommonStateEnum;
import com.acrel.exceptions.CommonException;
import com.acrel.exceptions.ExceptionEnum;
import com.acrel.utils.EncryptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @Description:
 * @Author: ZhouChenyu
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public BaseDAO<User> getDAO() {
        return userDAO;
    }

    @Override
    public <K> void beforeCreate(K k) {
        if (k instanceof User) {
            ((User)k).setState(CommonStateEnum.ON);
        }
    }

    @Override
    public boolean checkPassword(User user) {
        if (StringUtils.isEmpty(user.getAccount())) {
            throw new CommonException(ExceptionEnum.ACCOUNT_NULL);
        }
        if (StringUtils.isEmpty(user.getPassword())) {
            throw new CommonException(ExceptionEnum.PASSWORD_NULL);
        }
        String pwd = EncryptUtils.handler(user.getPassword());
        User dbUser = this.userDAO.getPasswordByAccount(user.getAccount());
        if (dbUser == null) {
            throw new CommonException(ExceptionEnum.USER_NULL);
        }
        if (!pwd.equals(dbUser.getPassword())) {
            throw new CommonException(ExceptionEnum.PASSWORD_ERROR);
        }
        return true;
    }

    @Override
    public User getUserByAccount(String account) {
        User dbUser = this.userDAO.getPasswordByAccount(account);
        return dbUser;
    }

    @Override
    public User changePassword(Integer id, String newPassword, String oldPassword) throws CommonException {
        User user = this.userDAO.get(id);
        if (user == null) {
            throw new CommonException(ExceptionEnum.USER_NULL);
        }
        String password = user.getPassword();
        String oldPwd = EncryptUtils.handler(oldPassword);
        if (!oldPwd.equals(password)) {
            throw new CommonException(ExceptionEnum.PASSWORD_ERROR);
        }
        user.setPassword(EncryptUtils.handler(newPassword));
        return this.update(user);
    }

    @Override
    public User freeze(Integer id) throws CommonException {
        return this.changeStatus(id, CommonStateEnum.OFF);
    }

    @Override
    public User active(Integer id) throws CommonException {
        return this.changeStatus(id, CommonStateEnum.ON);
    }

    @Override
    public String generateTokenByAccount(String account) throws CommonException {
        User user = this.userDAO.getByAccount(account);
        if (user == null) {
            throw new CommonException(ExceptionEnum.USER_NULL);
        }
        return Token.generate(user);
    }

    private User changeStatus(Integer id, CommonStateEnum state) throws CommonException {
        User user = this.userDAO.get(id);
        if (user == null) {
            throw new CommonException(ExceptionEnum.USER_NULL);
        }
        user.setState(state);
        return this.update(user);
    }

}
