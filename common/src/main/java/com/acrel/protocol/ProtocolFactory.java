package com.acrel.protocol;

import com.acrel.exceptions.CommonException;
import com.acrel.exceptions.ExceptionEnum;
import com.acrel.utils.SpringContextUtils;
import org.springframework.stereotype.Component;

@Component
public class ProtocolFactory {

    private final static String PACKAGE_PREFIX = "com.acrel.protocol.codec.";

    /**
     * 根据消息格式判断协议类型
     */
    private static ProtocolEnum parseMessage(Object msg) {
        return ProtocolEnum.parseMessage(msg);
    }

    /**
     * 根据消息内容解析协议
     * @param msg
     * @return
     */
    public static Protocol parse(Object msg) {
        ProtocolEnum protocolEnum = ProtocolEnum.parseMessage(msg);
        return new Protocol(getEncoder(protocolEnum),getDecoder(protocolEnum));
    }

    /**
     * 根据消息内容解析编码器
     * @param msg
     * @return
     */
    public static ProtocolEncoder parseEncoder(Object msg) {
        ProtocolEnum protocolEnum = ProtocolEnum.parseMessage(msg);
        return getEncoder(protocolEnum);
    }

    /**
     * 根据消息内容解析解码器
     * @param msg
     * @return
     */
    public static ProtocolDecoder parseDecoder(Object msg) {
        ProtocolEnum protocolEnum = parseMessage(msg);
        return getDecoder(protocolEnum);
    }

    /**
     * 根据名称获取编码器
     *
     * @param name
     * @return
     */
    public static ProtocolEncoder getEncoder(String name) {
        ProtocolEnum typeEnum = ProtocolEnum.getByName(name);
        return getEncoder(typeEnum);
    }

    /**
     * 根据名称获取解码器
     *
     * @param name
     * @return
     */
    public static ProtocolDecoder getDecoder(String name) {
        ProtocolEnum typeEnum = ProtocolEnum.getByName(name);
        return getDecoder(typeEnum);
    }

    /**
     * 根据协议类型，生成编码器
     *
     * @param protocolEnum
     * @return
     */
    private static ProtocolEncoder getEncoder(ProtocolEnum protocolEnum) {
        try {
            String classFullName = getClassFullName(protocolEnum.getEncoder());
            Class clazz = Class.forName(classFullName);
            Object ins = SpringContextUtils.getBean(clazz);
            if (ins instanceof ProtocolEncoder) {
                ProtocolEncoder handler = (ProtocolEncoder) ins;
                return handler;
            } else {
                throw new CommonException(ExceptionEnum.CLASS_CONVERT_ERROR);
            }
        } catch (ClassNotFoundException e) {
            throw new CommonException(ExceptionEnum.CLASS_NOT_FOUND);
        }
    }

    /**
     * 根据协议类型，生成解码器
     *
     * @param protocolEnum
     * @return
     */
    private static ProtocolDecoder getDecoder(ProtocolEnum protocolEnum) {
        try {
            String classFullName = getClassFullName(protocolEnum.getDecoder());
            Class clazz = Class.forName(classFullName);
            Object ins = SpringContextUtils.getBean(clazz);
            if (ins instanceof ProtocolDecoder) {
                ProtocolDecoder handler = (ProtocolDecoder) ins;
                return handler;
            } else {
                throw new CommonException(ExceptionEnum.CLASS_CONVERT_ERROR);
            }
        } catch (ClassNotFoundException e) {
            throw new CommonException(ExceptionEnum.CLASS_NOT_FOUND);
        }
    }

    private static String getClassFullName(String className) {
        return PACKAGE_PREFIX + className;
    }
}
