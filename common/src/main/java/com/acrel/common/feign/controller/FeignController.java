package com.acrel.common.feign.controller;

import com.acrel.common.feign.service.FeignService;
import com.acrel.entity.base.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: ZhouChenyu
 */
@Slf4j
public abstract class FeignController<T> {

    public abstract FeignService getService();

    @PostMapping(value = "/save")
    public Result<T> save(@RequestBody T t) {
        return this.getService().save(t);
    }

    @PostMapping(value = "/create")
    public Result<T> create(@RequestBody T t) {
        return this.getService().create(t);
    }

    @PostMapping(value = "/update")
    public Result<T> update(@RequestBody T t) {
        return this.getService().update(t);
    }

    @GetMapping(value = "/delete")
    public Result<T> delete(@RequestParam("id") String id) {
        return this.getService().delete(id);
    }

    @GetMapping(value = "/get")
    public Result<T> get(@RequestParam("id") String id) {
        return this.getService().get(id);
    }

    @PostMapping(value = "/list")
    public Result<T> list(@RequestBody Map params) {
        return this.getService().list(params);
    }

    @PostMapping(value = "/list_by_page")
    public Result<T> listByPage(@RequestBody Map params) {
        return this.getService().listByPage(params);
    }
}
