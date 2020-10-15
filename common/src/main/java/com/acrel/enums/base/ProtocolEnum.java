package com.acrel.enums.base;

/**
 * @Description:
 * @Author: ZhouChenyu
 */
public enum ProtocolEnum {

    HTTP("http"), HTTPS("https");

    ProtocolEnum(String protocol) {
        this.protocol = protocol;
    }

    private String protocol;

    public String getProtocol() {
        return protocol;
    }
}
