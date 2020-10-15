package com.acrel.common.feign.service.impl;

import com.acrel.common.feign.service.FeignService;
import com.acrel.entity.base.Result;
import com.acrel.exceptions.ExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: ZhouChenyu
 */
@Component
@Slf4j
public class FeignServiceHystrix<T> implements FeignService<T> {

    @Override
    public Result save(T t) {
        return new Result(ExceptionEnum.HYSTRIX);
    }

    @Override
    public Result create(T t) {
        return new Result(ExceptionEnum.HYSTRIX);
    }

    @Override
    public Result update(T t) {
        return new Result(ExceptionEnum.HYSTRIX);
    }

    @Override
    public Result delete(String id) {
        return new Result(ExceptionEnum.HYSTRIX);
    }

    @Override
    public Result get(String id) {
        return new Result(ExceptionEnum.HYSTRIX);
    }

    @Override
    public Result list(Map params) {
        return new Result(ExceptionEnum.HYSTRIX);
    }

    @Override
    public Result listByPage(Map params) {
        return new Result(ExceptionEnum.HYSTRIX);
    }
}
