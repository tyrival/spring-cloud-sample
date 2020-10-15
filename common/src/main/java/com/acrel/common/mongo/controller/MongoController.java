package com.acrel.common.mongo.controller;

import com.acrel.annotation.mvc.CurrentUser;
import com.acrel.common.mongo.service.MongoService;
import com.acrel.entity.base.PageQuery;
import com.acrel.entity.auth.User;
import com.acrel.entity.base.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

/**
 * @Author: ZhouChenyu
 */
@Slf4j
public abstract class MongoController<T> {

    private static final String DEFAULT_PAGE_INDEX = "1";
    private static final String DEFAULT_PAGE_SIZE = "15";
    private final static String PARAM_SOURCE = "source";
    private final static String PARAM_TARGET = "target";
    private final static String PARAM_PAGE_INDEX = "pageIndex";
    private final static String PARAM_PAGE_SIZE = "pageSize";

    public abstract MongoService getService();

    @PostMapping("/save")
    public Result save(@RequestBody T t, @CurrentUser User user) {
        return new Result(this.getService().save(t));
    }

    @PostMapping("/update_first")
    public Result updateFirst(@RequestBody Map<String, Map> params, @CurrentUser User user) {
        Map source = params.get(PARAM_SOURCE);
        Map target = params.get(PARAM_TARGET);
        long i = this.getService().updateFirst(source, target);
        return new Result(i > 0);
    }

    @PostMapping("/update_multi")
    public Result updateMulti(@RequestBody Map<String, Map> params, @CurrentUser User user) {
        Map source = params.get(PARAM_SOURCE);
        Map target = params.get(PARAM_TARGET);
        long i = this.getService().updateFirst(source, target);
        return new Result(i > 0);
    }

    @PostMapping("/update_insert")
    public Result updateInsert(@RequestBody Map<String, Map> params, @CurrentUser User user) {
        Map source = params.get(PARAM_SOURCE);
        Map target = params.get(PARAM_TARGET);
        long i = this.getService().updateFirst(source, target);
        return new Result(i > 0);
    }

    @GetMapping("/delete")
    public Result delete(@RequestBody T t) {
        long i = this.getService().delete(t);
        return new Result(i > 0);
    }

    @GetMapping("/delete_by_id")
    public Result deleteById(@RequestParam("id") Integer id) {
        long i = this.getService().deleteById(id);
        return new Result(i > 0);
    }

    @GetMapping("/find_by_id")
    public Result findById(@RequestParam("id") Integer id) {
        T t = (T) this.getService().findById(id);
        return new Result(t);
    }

    @GetMapping("/find_one")
    public Result findOne(@RequestBody T t) {
        t = (T) this.getService().findOne(t);
        return new Result(t);
    }

    @GetMapping("/find")
    public Result find(@RequestBody T t) {
        List<T> list = this.getService().find(t);
        return new Result(list);
    }

    @GetMapping("/find_by_page")
    public Result findByPage(@RequestBody Map params) {
        int pageIndex =
                Integer.parseInt(Optional.ofNullable(params.get(PARAM_PAGE_INDEX).toString()).orElse(DEFAULT_PAGE_INDEX));
        int pageSize =
                Integer.parseInt(Optional.ofNullable(params.get(PARAM_PAGE_SIZE).toString()).orElse(DEFAULT_PAGE_SIZE));
        PageQuery pageQuery = new PageQuery(pageIndex, pageSize);
        pageQuery.setParams(params);
        Result result = this.getService().findByPage(pageQuery);
        return result;
    }

    @GetMapping("/count")
    public Result count(@RequestBody T t) {
        long count = this.getService().count(t);
        return new Result(count);
    }
}
