package com.acrel.api;

import com.acrel.enums.base.ProtocolEnum;

/**
 * @Description:
 * @Author: ZhouChenyu
 */
public enum ApiEnum {

    USER_GET(ProtocolEnum.HTTP, ServiceName.SYSTEM, ControllerName.SAMPLE_USER, "/get"),

    BAIDU("https://www.baidu.com")
    ;

    ApiEnum(String url) {
        this.url = url;
    }

    ApiEnum(ProtocolEnum protocol, String service, String controller, String method) {
        this.protocol = protocol;
        this.service = service;
        this.controller = controller;
        this.method = method;
        this.url = this.protocol.getProtocol() + "://" + this.service + this.controller + this.method;
    }

    private ProtocolEnum protocol;

    private String service;

    public String controller;
    
    private String method;

    private String url;

    public String getUrl() {
        return this.url;
    }
}
