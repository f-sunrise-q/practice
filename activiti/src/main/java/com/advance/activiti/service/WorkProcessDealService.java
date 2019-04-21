package com.advance.activiti.service;

import com.advance.activiti.dto.ProcessDealDto;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qin on 19/4/21.
 */
@Service
public class WorkProcessDealService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    /**
     * 驳回到上一节点
     *
     * @param processDealDto
     * @throws Exception
     */
    public void revoke(ProcessDealDto processDealDto) throws Exception {

        Task task = taskService.createTaskQuery().processInstanceId(processDealDto.getInstanceId()).singleResult();
        if (task == null) {
            throw new Exception("流程未启动或已执行完成，无法撤回");
        }

//        LoginUser loginUser = SessionContext.getLoginUser();
        List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processDealDto.getInstanceId())
                .orderByTaskCreateTime()
                .desc()
                .list();

        HistoricTaskInstance myTask = htiList.get(1);
        String myTaskId = myTask.getId();
//        for (HistoricTaskInstance hti : htiList) {
//            if (processDealDto.getPersonId().equals(hti.getAssignee())) {
//                myTaskId = hti.getId();
//                myTask = hti;
//                break;
//            }
//        }
        if (null == myTaskId) {
            throw new Exception("该任务非当前用户提交，无法撤回");
        }

        String processDefinitionId = myTask.getProcessDefinitionId();
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);

        //变量
//		Map<String, VariableInstance> variables = runtimeService.getVariableInstances(currentTask.getExecutionId());
        String myActivityId = null;
        List<HistoricActivityInstance> haiList = historyService.createHistoricActivityInstanceQuery()
                .executionId(myTask.getExecutionId()).finished().list();
        for (HistoricActivityInstance hai : haiList) {
            if (myTaskId.equals(hai.getTaskId())) {
                myActivityId = hai.getActivityId();
                break;
            }
        }
        FlowNode myFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(myActivityId);


        Execution execution = runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
        String activityId = execution.getActivityId();
//        logger.warn("------->> activityId:" + activityId);
        FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(activityId);

        //记录原活动方向
        List<SequenceFlow> oriSequenceFlows = new ArrayList<SequenceFlow>();
        oriSequenceFlows.addAll(flowNode.getOutgoingFlows());

        //清理活动方向
        flowNode.getOutgoingFlows().clear();
        //建立新方向
        List<SequenceFlow> newSequenceFlowList = new ArrayList<SequenceFlow>();
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setId("newSequenceFlowId");
        newSequenceFlow.setSourceFlowElement(flowNode);
        newSequenceFlow.setTargetFlowElement(myFlowNode);
        newSequenceFlowList.add(newSequenceFlow);
        flowNode.setOutgoingFlows(newSequenceFlowList);

//        Authentication.setAuthenticatedUserId(processDealDto.getPersonId());
        taskService.addComment(task.getId(), task.getProcessInstanceId(), "撤回");

        taskService.setAssignee(task.getId(), processDealDto.getPersonId());


        Map<String, Object> currentVariables = new HashMap<String, Object>();
        currentVariables.put("check", 0);
        //完成任务
        taskService.complete(task.getId(), currentVariables);
        //恢复原方向
        flowNode.setOutgoingFlows(oriSequenceFlows);
    }

}
