package com.acrel.enums.quartz;

import com.acrel.entity.quartz.QuartzTask;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

/**
 * @Description:
 * @Author: ZhouChenyu
 */
public interface IQuartzHandler {

    void handler(Scheduler scheduler, QuartzTask task) throws SchedulerException;
}
