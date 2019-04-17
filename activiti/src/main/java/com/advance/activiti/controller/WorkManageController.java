package com.advance.activiti.controller;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/audit/test/v1")
public class WorkManageController {

    @Autowired
    private ProcessEngine processEngine;

    @GetMapping("/history")
    public String getHistoryTask(){
        String assignee = "WorkManager";
        String taskId = "";
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(assignee).list();
        return tasks.toString();
    }

    @GetMapping("/stop")
    public String stopTaskById(String taskId){
        TaskService taskService = processEngine.getTaskService();
        taskService.complete(taskId);
        return "success";
    }
}
