package com.acrel.exceptions;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * @Description:
 * @Author: ZhouChenyu
 */
public class CommonException extends RuntimeException implements Serializable {
    private ExceptionEnum codeEnum;

    public CommonException(ExceptionEnum codeEnum){
        super(codeEnum.getMessage());
        this.codeEnum = codeEnum;
    }

    public CommonException(){}

    public ExceptionEnum getCodeEnum() {
        return codeEnum;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
