package com.yuyan.starter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.task.listener.TaskExecutionListener;
import org.springframework.cloud.task.repository.TaskExecution;
import org.springframework.stereotype.Component;

@Component
public class MyTaskListener implements TaskExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(MyTaskListener.class);


    @Override
    public void onTaskStartup(TaskExecution taskExecution) {
        log.info("starting task:" + taskExecution.getTaskName());
    }

    @Override
    public void onTaskEnd(TaskExecution taskExecution) {
        log.info("ending task:" + taskExecution.getTaskName());
    }

    @Override
    public void onTaskFailed(TaskExecution taskExecution, Throwable throwable) {
        log.info("task failed:" + taskExecution.getTaskName());
        log.error("error msg:", throwable);
    }
}
