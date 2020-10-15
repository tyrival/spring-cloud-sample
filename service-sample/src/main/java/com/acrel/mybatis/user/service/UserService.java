package com.acrel.mybatis.user.service;

import com.acrel.common.base.service.BaseService;
import com.acrel.entity.auth.User;
import com.acrel.exceptions.CommonException;

/**
 * @Description:
 * @Author: ZhouChenyu
 */
public interface UserService extends BaseService<User> {

    /**
     * 验证账号密码
     * @param user
     * @return
     */
    boolean checkPassword(User user);

    /**
     * 根据账户查找用户
     * @param account
     * @return
     */
    User getUserByAccount(String account);

    /**
     * 修改密码
     * @param id
     * @param newPassword
     * @param oldPassword
     * @return
     * @throws Exception
     */
    User changePassword(Integer id, String newPassword, String oldPassword) throws Exception;

    /**
     * 冻结用户状态
     * @param id
     * @return
     * @throws Exception
     */
    User freeze(Integer id) throws CommonException;

    /**
     * 激活用户状态
     * @param id
     * @return
     * @throws Exception
     */
    User active(Integer id) throws CommonException;

    /**
     * 根据账号生成token
     * @param account
     * @return
     * @throws Exception
     */
    String generateTokenByAccount(String account) throws CommonException;

}
