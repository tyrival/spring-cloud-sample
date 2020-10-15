package com.acrel.aspect.quartz;

import com.acrel.annotation.quartz.QuartzSchedule;
import com.acrel.entity.quartz.QuartzTask;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @Author: ZhouChenyu
 */
@Aspect
@Component
public class QuartzScheduleAspect<T> {

    @Autowired
    private Scheduler scheduler;

    @Pointcut("@annotation(com.acrel.annotation.quartz.QuartzSchedule)")
    public void operatePoint() {
    }

    @After("operatePoint()&&@annotation(quartzSchedule)")
    public void operate(JoinPoint point, QuartzSchedule quartzSchedule) throws Throwable {
        if (this.scheduler == null) {
            return;
        }
        Object[] args = point.getArgs();
        if (args.length <= 0) {
            return;
        }
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();
        String argName = quartzSchedule.value();
        Object object = null;
        if (!StringUtils.isEmpty(argName)) {
            for (int i = 0; i < parameterNames.length; i++) {
                if (parameterNames[i].equals(argName)) {
                    object = args[i];
                    break;
                }
            }
        } else {
            object = args[0];
        }
        if (!(args[0] instanceof QuartzTask)) {
            return;
        }
        QuartzTask task = (QuartzTask) object;
        quartzSchedule.handler().handler(scheduler, task);
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }
}
