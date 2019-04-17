package com.advance.test;

import org.activiti.engine.*;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.Calendar;
import java.util.List;

public class MyLeaveActivitiTest {

//    @Test
//    public void creatTable(){
//        ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml").buildProcessEngine();
//    }

    //部署流程
    @Test
    public void deployProcess(){
        //初始化配置
        ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml").buildProcessEngine();
        //发布
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder builder = repositoryService.createDeployment();
        builder.addClasspathResource("jbpm/audit.bpmn");//bpmn文件的名称
        builder.deploy();

        //启动
        RuntimeService runtimeService =processEngine.getRuntimeService();
        //可根据id、key、message启动流程
        runtimeService.startProcessInstanceByKey("workAudit");

        //执行
        TaskService taskService =processEngine.getTaskService();
        //根据assignee(代理人)查询任务
        String assignee = "WorkManager";
        String taskId = "";
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(assignee).list();
        Calendar calendar = Calendar.getInstance();
        for (Task task : tasks) {
            taskId = task.getId();
            calendar.setTime(task.getCreateTime());
//            TaskService taskService =processEngine.getTaskService();

            taskService.complete(taskId);
        }
    }

}
