package com.acrel.mybatis.role.controller;

import com.acrel.annotation.mvc.CurrentUser;
import com.acrel.api.ControllerName;
import com.acrel.entity.auth.Role;
import com.acrel.entity.auth.User;
import com.acrel.entity.auth.UserRole;
import com.acrel.mybatis.role.service.UserRoleService;
import com.acrel.common.base.controller.BaseController;
import com.acrel.common.base.service.BaseService;
import com.acrel.entity.base.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户-角色管理
 */
@RestController
@RequestMapping(ControllerName.SAMPLE_USER_ROLE)
public class UserRoleController extends BaseController<UserRole> {

    @Autowired
    private UserRoleService userRoleService;

    /**
     * 保存用户的所有角色关系
     * @param userId
     * @param roleIds
     * @return
     */
    @GetMapping("/save_user_roles")
    public Result saveRolesOfUser(@RequestParam Integer userId,
                                  @RequestParam String roleIds,
                                  @CurrentUser User user) {
        this.userRoleService.deleteByUserId(userId);
        String[] roleIdArray = roleIds.split(ID_SEPARATOR);
        Integer createUserId = user == null ? null : user.getId();
        Date date = new Date();
        if (roleIdArray != null && roleIdArray.length > 0) {
            List<UserRole> list = new ArrayList<>();
            for (int i = 0; i < roleIdArray.length; i++) {
                Integer roleId = Integer.parseInt(roleIdArray[i]);
                UserRole userRole = new UserRole(userId, roleId);
                userRole.setCreateUserId(createUserId);
                userRole.setCreateTime(date);
                list.add(userRole);
            }
            int i = this.userRoleService.insertBatch(list);
        }
        return new Result();
    }

    /**
     * 保存角色的所有用户关系
     * @param userIds
     * @param roleId
     * @return
     */
    @GetMapping("/save_role_users")
    public Result saveUsersOfRole(@RequestParam String userIds,
                                  @RequestParam Integer roleId,
                                  @CurrentUser User user) {
        this.userRoleService.deleteByRoleId(roleId);
        String[] userIdArray = userIds.split(ID_SEPARATOR);
        Integer createUserId = user == null ? null : user.getId();
        Date date = new Date();
        if (userIdArray != null && userIdArray.length > 0) {
            List<UserRole> list = new ArrayList<>();
            for (int i = 0; i < userIdArray.length; i++) {
                Integer userId = Integer.parseInt(userIdArray[i]);
                UserRole userRole = new UserRole(userId, roleId);
                userRole.setCreateUserId(createUserId);
                userRole.setCreateTime(date);
                list.add(userRole);
            }
            int i = this.userRoleService.insertBatch(list);
        }
        return new Result();
    }

    /**
     * 删除用户的所有角色关系
     * @param userId
     * @return
     */
    @GetMapping("/delete_by_user_id")
    public Result deleteByUserId(@RequestParam Integer userId) {
        int i = this.userRoleService.deleteByUserId(userId);
        return new Result(i > 0);
    }

    /**
     * 删除角色的所有用户关系
     * @param roleId
     * @return
     */
    @GetMapping("/delete_by_role_id")
    public Result deleteByRoleId(@RequestParam Integer roleId) {
        int i = this.userRoleService.deleteByRoleId(roleId);
        return new Result(i > 0);
    }

    /**
     * 列出角色的所有用户
     * @param roleId
     * @return
     */
    @GetMapping("/list_user_by_role")
    public Result listUserByRole(@RequestParam Integer roleId) {
        List<User> list = this.userRoleService.listUserByRole(roleId);
        return new Result(list);
    }

    /**
     * 列出用户的所有角色
     * @param userId
     * @return
     */
    @GetMapping("/list_role_by_user")
    public Result listRoleByUser(@RequestParam Integer userId) {
        List<Role> list = this.userRoleService.listRoleByUser(userId);
        return new Result(list);
    }

    @Override
    public BaseService getService() {
        return userRoleService;
    }
}
