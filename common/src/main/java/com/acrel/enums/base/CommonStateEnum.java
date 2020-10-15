package com.acrel.enums.base;

import com.acrel.enums.BaseEnum;

/**
 * @Description:
 * @Author: ZhouChenyu
 */
public enum CommonStateEnum implements BaseEnum {

    ON(1, "启用"),
    OFF(0, "禁用");

    private int code;
    private String msg;

    CommonStateEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "BaseStateEnum{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}