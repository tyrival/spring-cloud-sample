package com.acrel.annotation.quartz;

import com.acrel.enums.quartz.QuartzHandlerEnum;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @Author: ZhouChenyu
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface QuartzSchedule {

    /**
     * 参数
     */
    @AliasFor("value")
    String value();

    /**
     * 任务操作
     * @return
     */
    @AliasFor("handler")
    QuartzHandlerEnum handler();
}
