package com.acrel.mybatis.user.controller;

import com.acrel.annotation.mvc.CurrentUser;
import com.acrel.api.ControllerName;
import com.acrel.entity.auth.User;
import com.acrel.mybatis.role.service.UserRoleService;
import com.acrel.mybatis.user.service.UserService;
import com.acrel.common.base.controller.BaseController;
import com.acrel.common.base.service.BaseService;
import com.acrel.entity.base.Result;
import com.acrel.entity.base.Token;
import com.acrel.exceptions.ExceptionEnum;
import com.acrel.utils.EncryptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: 用户管理
 * @Author: ZhouChenyu
 */
@RestController
@RequestMapping(ControllerName.SAMPLE_USER)
public class UserController extends BaseController<User> {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Override
    public BaseService getService() {
        return userService;
    }

    /**
     * 账号密码登陆
     *
     * @param user 必须传入account和password属性
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        boolean flag = this.userService.checkPassword(user);
        if (flag) {
            String token = this.userService.generateTokenByAccount(user.getAccount());
            return new Result(token);
        } else {
            return new Result(ExceptionEnum.PASSWORD_ERROR);
        }
    }

    /**
     * token登陆
     *
     * @param token
     * @return
     */
    @GetMapping("/login")
    public Result login(@RequestParam String token) {
        Token tok = Token.parse(token);
        User tokenUser = tok.getUser();
        User dbUser = this.userService.get(tokenUser.getId());
        if (dbUser == null ||
                !dbUser.getAccount().equals(tokenUser.getAccount()) ||
                !dbUser.getName().equals(tokenUser.getName()) ||
                !dbUser.getId().equals(tokenUser.getId()) ||
                !dbUser.getPassword().equals(tokenUser.getPassword())) {
            return new Result(ExceptionEnum.TOKEN_ERROR);
        }
        return new Result();
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @GetMapping("/delete")
    @Override
    public Result delete(@RequestParam("id") Integer id) {
        int i = this.userService.delete(id);
        this.userRoleService.deleteByUserId(id);
        return new Result(i > 0);
    }

    /**
     * 验证密码
     *
     * @param user 必须传入account和password属性
     * @return
     */
    @PostMapping("/check_password")
    public Result changePassword(@RequestBody User user) {
        boolean flag = this.userService.checkPassword(user);
        return new Result(flag);
    }

    /**
     * 用户修改自己的登陆密码
     *
     * @param originalPwd
     * @param newPwd
     * @param repeatPwd
     * @return
     */
    @GetMapping("/change_password")
    public Result changePassword(@RequestParam String originalPwd,
                                 @RequestParam String newPwd,
                                 @RequestParam String repeatPwd,
                                 @CurrentUser User user) {
        if (StringUtils.isEmpty(newPwd)) {
            return new Result(ExceptionEnum.PASSWORD_NULL);
        }
        if (!newPwd.equals(repeatPwd)) {
            return new Result(ExceptionEnum.PASSWORD_DIFFERENT);
        }
        user = this.userService.get(user.getId());
        user.setPassword(originalPwd);
        boolean check = this.userService.checkPassword(user);
        if (check) {
            user.setPassword(EncryptUtils.handler(newPwd));
            user = this.userService.update(user);
            return new Result();
        }
        return new Result(ExceptionEnum.UNKNOW_ERROR);
    }

    /**
     * 修改某个用户的密码
     *
     * @param userId
     * @param password
     * @return
     */
    @GetMapping("/change_password_user")
    public Result changePasswordUser(@RequestParam Integer userId, @RequestParam String password) {
        User user = this.userService.get(userId);
        user.setPassword(EncryptUtils.handler(password));
        this.userService.update(user);
        return new Result();
    }

    /**
     * 生成临时token
     *
     * @param id
     * @param expireTime 有效时间，单位：分钟
     * @return
     */
    @GetMapping("/token/temp")
    public Result temporaryToken(@RequestParam Integer id, Long expireTime) {
        User user = this.userService.get(id);
        if (user == null) {
            return new Result(ExceptionEnum.USER_NULL);
        }
        String token = Token.generate(user, expireTime);
        return new Result(token);
    }

    /**
     * 生成持久化token，账号或密码变更后失效
     *
     * @param id
     * @return
     */
    @GetMapping("/token/persist")
    public Result persistToken(@RequestParam Integer id) {
        User user = this.userService.get(id);
        if (user == null) {
            return new Result(ExceptionEnum.USER_NULL);
        }
        String token = Token.persistent(user);
        return new Result(token);
    }

}
