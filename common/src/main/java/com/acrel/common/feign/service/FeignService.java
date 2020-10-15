package com.acrel.common.feign.service;

import com.acrel.entity.base.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @Author: ZhouChenyu
 */
public interface FeignService<T> {

    @RequestMapping(value = "/save")
    Result<T> save(@RequestBody T t);

    @RequestMapping(value = "/create")
    Result<T> create(@RequestBody T t);

    @RequestMapping(value = "/update")
    Result<T> update(@RequestBody T t);

    @RequestMapping(value = "/delete")
    Result<T> delete(@RequestParam(value = "id") String id);

    @RequestMapping(value = "/get")
    Result<T> get(@RequestParam(value = "id") String id);

    @RequestMapping(value = "/list")
    Result list(@RequestBody Map params);

    @RequestMapping("/list_by_page")
    Result listByPage(@RequestBody Map params);
}
