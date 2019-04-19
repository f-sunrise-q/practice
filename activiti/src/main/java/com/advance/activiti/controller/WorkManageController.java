package com.advance.activiti.controller;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/audit/test/v1")
public class WorkManageController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private TaskService taskService;

    private static final Logger logger = LoggerFactory.getLogger(WorkManageController.class);

    private static final String PROCESS_ID = "workAudit";

    @GetMapping("/history")
    public String getHistoryTask(String instanceId){
        HistoricActivityInstanceQuery historyInstanceQuery = historyService.createHistoricActivityInstanceQuery().processInstanceId(instanceId);
        // 查询历史节点
        List<HistoricActivityInstance> historicActivityInstanceList = historyInstanceQuery.orderByHistoricActivityInstanceStartTime().asc().list();

        // 已执行的节点ID集合(将historicActivityInstanceList中元素的activityId字段取出封装到executedActivityIdList)
//        List<String> executedActivityIdList = historicActivityInstanceList.stream().map(item -> item.getActivityId()).collect(Collectors.toList());

//        logger.info("已执行的节点ID集合:" + executedActivityIdList);

        return historicActivityInstanceList.toString();
    }

//    @GetMapping("/stop")
//    public String stopTaskById(String taskId){
//        TaskService taskService = processEngine.getTaskService();
//        taskService.complete(taskId);
//        return "success";
//    }

    /**
     * 启动流程
     *
     * @return
     */
    @GetMapping("/start")
    public String startProcess(){
        // xml中定义的ID
        String instanceKey = PROCESS_ID;
        logger.info("开启作业流程...");

        // 设置流程参数，开启流程
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("jobNumber","A1001");
        map.put("busData","bus data");
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(instanceKey, map);//使用流程定义的key启动流程实例，key对应helloworld.bpmn文件中id的属性值，使用key值启动，默认是按照最新版本的流程定义启动

        logger.info("启动流程实例成功:{}", instance);
        logger.info("流程实例ID:{}", instance.getId());
        logger.info("流程定义ID:{}", instance.getProcessDefinitionId());


        //验证是否启动成功
        //通过查询正在运行的流程实例来判断
        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
        //根据流程实例ID来查询
        List<ProcessInstance> runningList = processInstanceQuery.processInstanceId(instance.getProcessInstanceId()).list();
        logger.info("根据流程ID查询条数:{}", runningList.size());

        // 返回流程ID
        return instance.getId();
    }

    /**
     * 提交任务
     *
     * @param workNo
     * @param workLeaderId
     * @return
     */
    @GetMapping("/commit")
    public String commitTask(String instanceId, String workNo, String workLeaderId){
//        String taskId = UUID.randomUUID().toString().replace("-", "");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("workNo", workNo);
        map.put("workLeaderId", workLeaderId);

        Task task = taskService.createTaskQuery().processInstanceId(instanceId).singleResult();

//        Task task = taskService.newTask(taskId);
        task.setOwner("zhangsan");
        task.setDescription("工作签发人提交作业");
        task.setName("aa");
//        taskService.
        String taskId = task.getId();


        taskService.saveTask(task);
        taskService.createAttachment("", taskId, instanceId, "msg", "创建作业", "");
        taskService.complete(taskId, map);
        return taskId;
    }

    /**
     * 查询任务
     *
     * @param taskId
     * @return
     */
    @GetMapping("/select/task")
    public String commitTask(String taskId){
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return task.toString();
    }

    /**
     * 一级审核
     *
     * @param instanceId
     * @return
     */
    @GetMapping("/task/confirm")
    public String confirmTask(String instanceId, String confirmResult){
        Task task = taskService.createTaskQuery().processInstanceId(instanceId).singleResult();
        if(task!=null){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("confirmResult", confirmResult);
            taskService.complete(task.getId(), map);
        }
        return "success";
    }

    /**
     * 二级审核
     *
     * @param instanceId
     * @return
     */
    @GetMapping("/task/confirm2")
    public String confirmTask2(String instanceId, String confirmResult){
        Task task = taskService.createTaskQuery().processInstanceId(instanceId).singleResult();
        if(task!=null){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("confirmResult", confirmResult);
            taskService.complete(task.getId(), map);
        }
        return "success";
    }

    @GetMapping("instance/end")
    public String instanceEnd(String instanceId){
        return isFinished(instanceId)==true?"1":"0";
    }

    /**
     * <p>判断流程是否完成</p>
     * @param processInstanceId 流程实例ID
     * @return boolean 已完成-true，未完成-false
     * @author FRH
     * @time 2018年12月10日上午11:23:26
     * @version 1.0
     */
    public boolean isFinished(String processInstanceId) {
        return historyService.createHistoricProcessInstanceQuery().finished().processInstanceId(processInstanceId).count() > 0;
    }
}
