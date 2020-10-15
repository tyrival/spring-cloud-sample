package com.acrel.enums.auth;

import com.acrel.enums.BaseEnum;

public enum UserTypeEnum implements BaseEnum {

    NORMAL(0, "普通用户"),
    VIP(1, "VIP用户");

    private int code;
    private String msg;

    UserTypeEnum(int code, String msg) {
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
        return "UserTypeEnum{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}