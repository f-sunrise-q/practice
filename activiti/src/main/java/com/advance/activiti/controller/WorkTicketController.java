package com.advance.activiti.controller;

import com.advance.activiti.constant.WorkFlowStatus;
import com.advance.activiti.dto.ProcessDealDto;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/workTicket/v1")
public class WorkTicketController {

    private static final Logger logger = LoggerFactory.getLogger(WorkTicketController.class);

    public static final String PROCESS_WORKTICKET_ID = "workTicketProcess";

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private TaskService taskService;

    /**
     * 启动作业票流程
     *
     * @return
     */
    @GetMapping("/start")
    public String startProcess(String workNo, String workContent, String personId){
        // xml中定义的ID
        String instanceKey = PROCESS_WORKTICKET_ID;
        logger.info("开启作业流程...");

        // 设置流程参数，开启流程
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("workNo",workNo);
        map.put("workContent",workContent);
        map.put("personId", personId);
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
     * 作业进度查询
     *
     * @param instanceId
     * @return
     */
    @GetMapping("/history")
    public String getHistoryTask(String instanceId){
        HistoricActivityInstanceQuery historyInstanceQuery = historyService.createHistoricActivityInstanceQuery().processInstanceId(instanceId);
        // 查询历史节点
        List<HistoricActivityInstance> historicActivityInstanceList = historyInstanceQuery.orderByHistoricActivityInstanceStartTime().asc().list();

        // 已执行的节点ID集合(将historicActivityInstanceList中元素的activityId字段取出封装到executedActivityIdList)
        List<String> executedActivityIdList = historicActivityInstanceList.stream().map(item -> item.getActivityId()).collect(Collectors.toList());

        logger.info("已执行的节点ID集合:" + executedActivityIdList);

        return historicActivityInstanceList.toString();
    }

    /**
     * 个人待办查询
     *
     * @param personId
     * @return
     */
    @GetMapping("/toDoList")
    public String getToDoListByPersonId(String personId){
        // 参与者，组任务查询
        List<Task> list = taskService.createTaskQuery().taskCandidateOrAssigned(personId).list();

        if(list!=null){
            return list.toString();
        }
        return null;
    }

    /**
     * 提交任务
     *
     * @param instanceId
     * @param workNo
     * @param workContent
     * @param personId
     * @return
     */
    @GetMapping("/commit")
    public String commitTask(String instanceId, String workNo, String workContent, String personId){
//        String taskId = UUID.randomUUID().toString().replace("-", "");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("workNo", workNo);
        map.put("personId", personId);

        Task task = taskService.createTaskQuery().processInstanceId(instanceId).singleResult();

//        Task task = taskService.newTask(taskId);
        task.setOwner(personId);
        task.setDescription("工作签发人提交作业");
        task.setName(workContent);
        task.setAssignee(personId);
//        taskService.
        String taskId = task.getId();



        taskService.createAttachment("", taskId, instanceId, "msg", "创建作业并提交流程", "");
        taskService.saveTask(task);
//        taskService.complete(taskId, map);
        return taskId;
    }

    /**
     * 工作许可人确认前，工作签发人删除
     *
     * @param instanceId
     * @param workNo
     * @param workContent
     * @param personId
     * @return
     */
    @GetMapping("/delete")
    public String deleteTask(String instanceId, String workNo, String workContent, String personId){
//        String taskId = UUID.randomUUID().toString().replace("-", "");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("workNo", workNo);
        map.put("personId", personId);
        map.put("delete", true);

        Task task = taskService.createTaskQuery().processInstanceId(instanceId).singleResult();

//        Task task = taskService.newTask(taskId);
        task.setOwner(personId);
        task.setDescription("工作签发人提交作业");
        task.setName(workContent);
        task.setAssignee(personId);
//        taskService.
        String taskId = task.getId();


        taskService.saveTask(task);
        taskService.createAttachment("", taskId, instanceId, "msg", "工作签发人删除", "");
        taskService.complete(taskId, map);
        return taskId;
    }

    /**
     * 工作许可人确认
     *
     * @param instanceId
     * @param checkResult
     * @param personId
     * @return
     */
    @GetMapping("/check1")
    public String commitTask(String instanceId, String checkResult, String personId){
//        String taskId = UUID.randomUUID().toString().replace("-", "");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("checkResult", checkResult);
        map.put("personId", personId);
        map.put("delete", false);

        Task task = taskService.createTaskQuery().processInstanceId(instanceId).singleResult();

        task.setDescription("工作许可人确认作业");

        task.setAssignee(personId);
//        taskService.
        String taskId = task.getId();
        taskService.saveTask(task);
        taskService.createAttachment("", taskId, instanceId, "msg", "工作许可人确认作业", "");
        taskService.complete(taskId, map);
        return taskId;
    }

    /**
     * 通用流程节点处理接口
     *
     * @param processDealDto
     * @return
     */
    @GetMapping("/check2")
    public String commitTaskByWorkLeader(ProcessDealDto processDealDto){
//        String taskId = UUID.randomUUID().toString().replace("-", "");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("check", processDealDto.getCheck());
        map.put("personId", processDealDto.getPersonId());
//        map.put("delete", false);

        Task task = taskService.createTaskQuery().processInstanceId(processDealDto.getInstanceId()).singleResult();

        task.setDescription(WorkFlowStatus.getDescriptionByIndex(processDealDto.getNodeType()));

        task.setAssignee(processDealDto.getPersonId());
//        taskService.
        String taskId = task.getId();
        taskService.saveTask(task);
        taskService.createAttachment("", taskId, processDealDto.getInstanceId(), "msg", WorkFlowStatus.getDescriptionByIndex(processDealDto.getNodeType()), "");
        taskService.complete(taskId, map);
        return taskId;
    }
}
