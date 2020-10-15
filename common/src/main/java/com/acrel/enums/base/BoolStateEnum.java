package com.acrel.enums.base;

import com.acrel.enums.BaseEnum;

/**
 * @Description:
 * @Author: ZhouChenyu
 */
public enum BoolStateEnum implements BaseEnum {

    FALSE(0, "否"),
    TRUE(1, "是");

    private int code;
    private String msg;

    BoolStateEnum(int code, String msg) {
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
        return "BoolStateEnum{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}