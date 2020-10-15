package com.acrel.common.base.controller;

import com.acrel.common.base.service.BaseService;
import com.acrel.entity.base.*;
import com.acrel.entity.auth.User;
import com.acrel.enums.base.BoolStateEnum;
import com.acrel.exceptions.ExceptionEnum;
import com.acrel.annotation.mvc.CurrentUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Author: ZhouChenyu
 */
@Slf4j
public abstract class BaseController<T> {

    public static final String ID_SEPARATOR = ",";
    private static final String DEFAULT_PAGE_INDEX = "1";
    private static final String DEFAULT_PAGE_SIZE = "15";
    private final static String PARAM_PAGE_INDEX = "pageIndex";
    private final static String PARAM_PAGE_SIZE = "pageSize";

    public abstract BaseService getService();

    @PostMapping("/save")
    public Result save(@RequestBody T t, @CurrentUser User user) {
        if (!(t instanceof Base)) {
            return new Result(ExceptionEnum.CLASS_CONVERT_ERROR);
        }
        Base base = (Base) t;
        Integer userId = user == null ? null : user.getId();
        if (StringUtils.isEmpty(base.getId())) {
            base.setCreateTime(new Date());
            base.setCreateUserId(userId);
            base.setDelFlag(BoolStateEnum.FALSE);
            t = (T) this.getService().create(base);
        } else {
            base.setUpdateUserId(userId);
            base.setUpdateTime(new Date());
            t = (T) this.getService().update(t);
        }
        return new Result(t);
    }

    @PostMapping("/create")
    public Result create(@RequestBody T t, @CurrentUser User user) {
        if (t instanceof Base) {
            Base base = (Base) t;
            Date current = Calendar.getInstance().getTime();
            base.setCreateTime(current);
            base.setUpdateTime(current);
            base.setDelFlag(BoolStateEnum.FALSE);
            if (user != null) {
                base.setCreateUserId(user.getId());
                base.setUpdateUserId(user.getId());
            }
            t = (T) this.getService().create(base);
        } else {
            t = (T) this.getService().create(t);
        }
        return new Result(t);
    }

    @PostMapping("/update")
    public Result update(@RequestBody T t, @CurrentUser User user) {
        if (t instanceof Base) {
            Base base = (Base) t;
            base.setUpdateTime(Calendar.getInstance().getTime());
            if (user != null) {
                base.setUpdateUserId(user.getId());
            }
            t = (T) this.getService().update(base);
        } else {
            t = (T) this.getService().update(t);
        }
        return new Result(t);
    }

    @GetMapping("/delete")
    public Result delete(@RequestParam("id") Integer id) {
        int i = this.getService().delete(id);
        Result result = new Result();
        result.setSuccess(i > 0);
        return result;
    }

    @GetMapping("/get")
    public Result get(@RequestParam("id") Integer id) {
        T t = (T) this.getService().get(id);
        return new Result(t);
    }

    @PostMapping(value = "/list")
    public Result list(@RequestBody Map params) {
        return new Result<>(this.getService().list(params));
    }

    @PostMapping("/list_by_page")
    public Result listByPage(@RequestBody Map params) {
        int pageIndex =
                Integer.parseInt(Optional.ofNullable(params.get(PARAM_PAGE_INDEX).toString()).orElse(DEFAULT_PAGE_INDEX));
        int pageSize =
                Integer.parseInt(Optional.ofNullable(params.get(PARAM_PAGE_SIZE).toString()).orElse(DEFAULT_PAGE_SIZE));
        PageQuery pageQuery = new PageQuery(pageIndex, pageSize);
        pageQuery.setParams(params);
        return this.getService().listByPage(pageQuery);
    }

    protected String[] splitIds(String ids) {
        return ids.split(ID_SEPARATOR);
    }
}
