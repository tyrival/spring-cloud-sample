package com.acrel.protocol;

import com.acrel.exceptions.CommonException;
import com.acrel.exceptions.ExceptionEnum;
import org.apache.commons.lang3.StringUtils;

public enum ProtocolEnum {

    SAMPLE("sample", "SampleProtocolEncoder", "SampleProtocolDecoder"),

    ;


    /**
     * 协议名称
     */
    private String name;

    /**
     * 协议使用的encoder
     */
    private String encoder;

    /**
     * 协议使用的decoder
     */
    private String decoder;


    ProtocolEnum(String name, String encoder, String decoder) {
        this.name = name;
        this.encoder = encoder;
        this.decoder = decoder;
    }

    /**
     * 根据消息格式，判断协议类型
     * @param msg
     * @return
     */
    public static ProtocolEnum parseMessage(Object msg) {

        // TODO 解析消息的内容，从而判断协议类型

        return SAMPLE;
    }

    public static ProtocolEnum getByName(String name) {
        for (ProtocolEnum type : ProtocolEnum.values()) {
            if (StringUtils.equals(type.name, name)) {
                return type;
            }
        }
        throw new CommonException(ExceptionEnum.ENUM_INVALID);
    }

    public String getName() {
        return name;
    }

    public String getEncoder() {
        return encoder;
    }

    public String getDecoder() {
        return decoder;
    }
}
