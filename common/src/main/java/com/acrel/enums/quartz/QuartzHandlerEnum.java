package com.acrel.enums.quartz;

import com.acrel.entity.quartz.QuartzTask;
import com.acrel.enums.BaseEnum;
import com.acrel.exceptions.CommonException;
import com.acrel.exceptions.ExceptionEnum;
import org.quartz.*;
import org.springframework.util.StringUtils;

/**
 * @Description:
 * @Author: ZhouChenyu
 */
public enum QuartzHandlerEnum implements IQuartzHandler, BaseEnum {

    CREATE(0, "创建任务") {
        @Override
        public void handler(Scheduler scheduler, QuartzTask quartzTask) {
            try {
                this.createJob(scheduler, quartzTask);
            } catch (SchedulerException e) {
                throw new CommonException(ExceptionEnum.QUARTZ_SCHEDULE_CREATE_FAIL);
            }
        }
    },
    UPDATE(1, "更新任务") {
        @Override
        public void handler(Scheduler scheduler, QuartzTask quartzTask) {
            try {
                this.removeJob(scheduler, quartzTask);
                this.createJob(scheduler, quartzTask);
            } catch (SchedulerException e) {
                throw new CommonException(ExceptionEnum.QUARTZ_SCHEDULE_REMOVE_FAIL);
            }
        }
    },
    REMOVE(2, "移除任务") {
        @Override
        public void handler(Scheduler scheduler, QuartzTask quartzTask) {
            try {
                this.removeJob(scheduler, quartzTask);
            } catch (SchedulerException e) {
                throw new CommonException(ExceptionEnum.QUARTZ_SCHEDULE_REMOVE_FAIL);
            }
        }
    },
    TRIGGER(3, "触发任务") {
        @Override
        public void handler(Scheduler scheduler, QuartzTask quartzTask) {
            try {
                this.validate(quartzTask);
                JobKey jobKey = new JobKey(quartzTask.getId(), quartzTask.getTaskGroup().getMsg());
                scheduler.triggerJob(jobKey);
            } catch (SchedulerException e) {
                throw new CommonException(ExceptionEnum.QUARTZ_SCHEDULE_TRIGGER_FAIL);
            }
        }
    },
    PAUSE(4, "暂停任务") {
        @Override
        public void handler(Scheduler scheduler, QuartzTask quartzTask) {
            try {
                this.validate(quartzTask);
                JobKey jobKey = new JobKey(quartzTask.getId(), quartzTask.getTaskGroup().getMsg());
                scheduler.pauseJob(jobKey);
            } catch (SchedulerException e) {
                throw new CommonException(ExceptionEnum.QUARTZ_SCHEDULE_PAUSE_FAIL);
            }
        }
    },
    RESUME(5, "恢复任务") {
        @Override
        public void handler(Scheduler scheduler, QuartzTask quartzTask) {
            try {
                this.validate(quartzTask);
                JobKey jobKey = new JobKey(quartzTask.getId(), quartzTask.getTaskGroup().getMsg());
                scheduler.resumeJob(jobKey);
            } catch (SchedulerException e) {
                throw new CommonException(ExceptionEnum.QUARTZ_SCHEDULE_RESUME_FAIL);
            }
        }
    },
    ;

    private int code;
    private String msg;

    QuartzHandlerEnum(int code, String msg) {
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
        return "QuartzHandlerEnum{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

    /**
     * 创建任务
     * @param scheduler
     * @param task
     * @throws SchedulerException
     * @throws CommonException
     */
    public void createJob(Scheduler scheduler, QuartzTask task) throws SchedulerException, CommonException {
        this.validate(task);
        if (StringUtils.isEmpty(task.getCron())) {
            throw new CommonException(ExceptionEnum.QUARTZ_SCHEDULE_CRON_NULL);
        }
        JobDetail job = JobBuilder.newJob(QuartzTask.class)
                .withIdentity(task.getId(), task.getTaskGroup().getMsg())
                .withDescription(task.getName()).build();
        // 触发时间点
        CronScheduleBuilder cronScheduleBuilder =
                CronScheduleBuilder.cronSchedule(task.getCron().trim());
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(task.getId(), task.getTaskGroup().getMsg())
                .startNow().withSchedule(cronScheduleBuilder).build();
        // Scheduler计划触发
        scheduler.scheduleJob(job, trigger);
    }

    /**
     * 移除任务
     * @param scheduler
     * @param quartzTask
     * @throws SchedulerException
     * @throws CommonException
     */
    public void removeJob(Scheduler scheduler, QuartzTask quartzTask) throws SchedulerException, CommonException {
        this.validate(quartzTask);
        String jobName = quartzTask.getId();
        String jobGroup = quartzTask.getTaskGroup().getMsg();
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        // 停止触发器
        scheduler.pauseTrigger(triggerKey);
        // 移除触发器
        scheduler.unscheduleJob(triggerKey);
        // 删除任务
        scheduler.deleteJob(JobKey.jobKey(jobName, jobGroup));
    }

    /**
     * 校验
     * @param task
     * @throws CommonException
     */
    public void validate(QuartzTask task) throws CommonException {
        if (StringUtils.isEmpty(task.getId())) {
            throw new CommonException(ExceptionEnum.QUARTZ_SCHEDULE_ID_NULL);
        }
        if (task.getTaskGroup() == null) {
            throw new CommonException(ExceptionEnum.QUARTZ_SCHEDULE_GROUP_NULL);
        }
    }
}