package com.acrel.exceptions;

import java.io.Serializable;

import static com.acrel.exceptions.ExpCodePrefix.*;


/**
 * @Description:
 * @Author: ZhouChenyu
 */
public enum ExceptionEnum implements Serializable {

    /**
     * 通用异常
     */
    UNKNOW_ERROR(COMMON_PREFIX + "000", "未知异常"),
    ERROR_404(COMMON_PREFIX + "001", "服务404"),
    PARAM_NULL(COMMON_PREFIX + "002", "参数为空"),
    NO_REPEAT(COMMON_PREFIX + "003", "请勿重复提交"),
    TOKEN_NULL(COMMON_PREFIX + "004", "TOKEN不存在"),
    TOKEN_EXPIRED(COMMON_PREFIX + "005", "TOKEN超时"),
    TOKEN_ERROR(COMMON_PREFIX + "006", "TOKEN错误"),
    SAVE_FAIL(COMMON_PREFIX + "010", "保存失败"),
    CREATE_FAIL(COMMON_PREFIX + "011", "新增失败"),
    UPDATE_FAIL(COMMON_PREFIX + "012", "更新失败"),
    DELETE_FAIL(COMMON_PREFIX + "013", "删除失败"),
    QUERY_FAIL(COMMON_PREFIX + "014", "查询失败"),
    API_INVALID(COMMON_PREFIX + "015", "接口无效"),
    CLASS_CONVERT_ERROR(COMMON_PREFIX + "020", "类型转换错误"),
    NEW_INSTANCE_ERROR(COMMON_PREFIX + "021", "对象实例化失败"),
    CLASS_NOT_FOUND(COMMON_PREFIX + "022", "未找到类"),
    SERVICE_INVOKE_ERROR(COMMON_PREFIX + "023", "远程服务调用失败"),
    ENUM_INVALID(COMMON_PREFIX + "024", "枚举无效"),

    QUARTZ_SCHEDULE_CREATE_FAIL(COMMON_PREFIX + "801", "定时任务创建失败"),
    QUARTZ_SCHEDULE_REMOVE_FAIL(COMMON_PREFIX + "802", "定时任务移除失败"),
    QUARTZ_SCHEDULE_TRIGGER_FAIL(COMMON_PREFIX + "803", "定时任务触发失败"),
    QUARTZ_SCHEDULE_PAUSE_FAIL(COMMON_PREFIX + "804", "定时任务暂停失败"),
    QUARTZ_SCHEDULE_RESUME_FAIL(COMMON_PREFIX + "805", "定时任务恢复失败"),
    QUARTZ_SCHEDULE_ID_NULL(COMMON_PREFIX + "806", "定时任务未设置ID"),
    QUARTZ_SCHEDULE_GROUP_NULL(COMMON_PREFIX + "807", "定时任务未设置分组"),
    QUARTZ_SCHEDULE_CRON_NULL(COMMON_PREFIX + "808", "定时任务未设置定时规则"),

    URL_ENCODE_ERROR(COMMON_PREFIX + "901", "URL编码失败"),
    URL_DECODE_ERROR(COMMON_PREFIX + "902", "URL解码失败"),
    FILE_NOT_FOUND(COMMON_PREFIX + "903", "未找到文件"),
    FILE_TYPE_ERROR(COMMON_PREFIX + "904", "文件类型错误"),
    EXCEL_READ_FAIL(COMMON_PREFIX + "905", "Excel文件读取失败"),
    EXCEL_FIRST_ROW_EMPTY(COMMON_PREFIX + "906", "Excel文件第一行为空"),

    /**
     * Feign模块异常
     */
    HYSTRIX(FEIGN_PREFIX + "000", "服务暂时失效，请重试"),

    /**
     * User模块异常
     */
    ACCOUNT_NULL(USER_PREFIX + "000", "用户名为空"),
    USER_NULL(USER_PREFIX + "001", "用户不存在"),
    USER_INVALID(USER_PREFIX + "002", "用户失效"),
    PASSWORD_NULL(USER_PREFIX + "011", "密码为空"),
    PASSWORD_ERROR(USER_PREFIX + "012", "密码错误"),
    PASSWORD_CHANGE_FAIL(USER_PREFIX + "013", "密码修改失败"),
    PASSWORD_DIFFERENT(USER_PREFIX + "014", "两次输入的密码不一致"),

    /**
     * File模块异常
     */
    UPLOAD_FAIL(FILE_PREFIX + "000", "文件上传失败"),
    FILE_NOT_EXIST(FILE_PREFIX + "001", "文件不存在"),


    ;

    private String code;
    private String message;

    ExceptionEnum() {
    }

    ExceptionEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }


    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
