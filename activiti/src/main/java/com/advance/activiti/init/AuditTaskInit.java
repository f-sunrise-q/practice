package com.advance.activiti.init;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AuditTaskInit implements CommandLineRunner {

    @Autowired
    private ProcessEngine processEngine;

    @Override
    public void run(String... args) throws Exception {

        //发布
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder builder = repositoryService.createDeployment();
        builder.addClasspathResource("jbpm/audit.bpmn");//bpmn文件的名称
        builder.deploy();

        //启动
        RuntimeService runtimeService =processEngine.getRuntimeService();
        //可根据id、key、message启动流程
        runtimeService.startProcessInstanceByKey("workAudit");
    }
}
