package com.acrel.entity.base;

import com.acrel.exceptions.CommonException;
import com.acrel.exceptions.ExceptionEnum;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description:
 * @Author: ZhouChenyu
 */
@Data
public class Result<T> implements Serializable {

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 错误代码
     */
    private String errorCode;

    /**
     * 消息
     */
    private String message;

    /**
     * 分页结果
     */
    private Page page;

    /**
     * 结果数据集
     */
    private T data;

    public Result() {
        this.success = true;
    }

    public Result(Boolean success) {
        this.success = success;
    }

    public Result(T data) {
        this.success = true;
        this.data = data;
    }

    public Result(Boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public Result(T data, Page page) {
        this.success = true;
        this.page = page;
        this.data = data;
    }

    public Result(CommonException exception) {
        this.success = false;
        this.errorCode = exception.getCodeEnum().getCode();
        this.message = exception.getCodeEnum().getMessage();
    }

    public Result(ExceptionEnum exception) {
        this.success = false;
        this.errorCode = exception.getCode();
        this.message = exception.getMessage();
    }

    public Result(T data, CommonException exception) {
        this.success = false;
        this.data = data;
        this.errorCode = exception.getCodeEnum().getCode();
        this.message = exception.getCodeEnum().getMessage();
    }

    public Result(T data, ExceptionEnum exception) {
        this.success = false;
        this.data = data;
        this.errorCode = exception.getCode();
        this.message = exception.getMessage();
    }

    public Result(T data, Page page, CommonException exception) {
        this.success = false;
        this.data = data;
        this.page = page;
        this.errorCode = exception.getCodeEnum().getCode();
        this.message = exception.getCodeEnum().getMessage();
    }

    public Result(T data, Page page, ExceptionEnum exception) {
        this.success = false;
        this.data = data;
        this.page = page;
        this.errorCode = exception.getCode();
        this.message = exception.getMessage();
    }

    @Override
    public String toString() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        // 使gson可以序列化特殊的double值(NaN, Infinity, -infinity)而不抛出异常
        gsonBuilder.serializeSpecialFloatingPointValues();
        Gson gson = gsonBuilder.create();
        return gson.toJson(this);
    }
}
