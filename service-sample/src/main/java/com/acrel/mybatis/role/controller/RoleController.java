package com.acrel.mybatis.role.controller;

import com.acrel.api.ControllerName;
import com.acrel.entity.auth.Role;
import com.acrel.mybatis.role.service.RoleService;
import com.acrel.mybatis.role.service.UserRoleService;
import com.acrel.common.base.controller.BaseController;
import com.acrel.common.base.service.BaseService;
import com.acrel.entity.base.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理
 */
@RestController
@RequestMapping(ControllerName.SAMPLE_ROLE)
public class RoleController extends BaseController<Role> {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleService userRoleService;

    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    @GetMapping("/delete")
    @Override
    public Result delete(@RequestParam("id") Integer id) {
        //删除角色信息
        int i = this.roleService.delete(id);
        //删除角色与用户的关联信息
        this.userRoleService.deleteByRoleId(id);
        return new Result(i > 0);
    }

    /**
     * 列出角色及相关的用户信息
     *
     * @param role
     * @return
     */
    @GetMapping("/list_cascade")
    public Result listCascade(@RequestBody Role role) {
        List<Role> roleList = this.roleService.listCascade(role);
        return new Result(roleList);
    }

    @Override
    public BaseService getService() {
        return roleService;
    }
}
