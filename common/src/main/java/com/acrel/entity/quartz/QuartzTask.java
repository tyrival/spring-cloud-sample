package com.acrel.entity.quartz;

import com.acrel.enums.base.CommonStateEnum;
import com.acrel.enums.quartz.QuartzGroupEnum;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @Description: 定时任务类
 * @Author: ZhouChenyu
 */
@Data
@ToString
public abstract class QuartzTask extends QuartzJobBean {

    /**
     * ID
     */
    @Id
    private String id;

    /**
     * 任务组
     */
    private QuartzGroupEnum taskGroup;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 任务组
     */
    private QuartzGroupEnum jobGroup;

    /**
     * 任务描述
     */
    private String jobDescription;

    /**
     * 定时规则
     */
    private String cron;

    /**
     * 启用状态：ON-启用 OFF-禁用
     */
    private CommonStateEnum activeState;
}
