package com.acrel.enums.charging;

import com.acrel.enums.BaseEnum;

public enum MenuTypeEnum implements BaseEnum {

    SAMPLE(0, "普通用户"),

    ;

    private int code;
    private String msg;

    MenuTypeEnum(int code, String msg) {
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
        return "MenuTypeEnum{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}