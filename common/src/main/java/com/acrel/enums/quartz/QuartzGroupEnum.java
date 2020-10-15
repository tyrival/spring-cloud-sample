package com.acrel.enums.quartz;

import com.acrel.enums.BaseEnum;

/**
 * @Description:
 * @Author: ZhouChenyu
 */
public enum QuartzGroupEnum implements BaseEnum {

    SAMPLE(0, "sample"),
    ;

    private int code;
    private String msg;

    QuartzGroupEnum(int code, String msg) {
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
        return "QuartzGroupEnum{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}