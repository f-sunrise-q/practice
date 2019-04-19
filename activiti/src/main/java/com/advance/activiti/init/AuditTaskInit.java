package com.advance.activiti.init;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.spring.SpringAsyncExecutor;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

@Component
public class AuditTaskInit implements CommandLineRunner {

//    @Autowired
//    private ProcessEngine processEngine;

    @Override
    public void run(String... args) throws Exception {

//        //发布
//        RepositoryService repositoryService = processEngine.getRepositoryService();
//        DeploymentBuilder builder = repositoryService.createDeployment();
//        builder.addClasspathResource("processes/audit.bpmn");//bpmn文件的名称
//        builder.deploy();
//
//        //启动
//        RuntimeService runtimeService =processEngine.getRuntimeService();
//        //可根据id、key、message启动流程
//        runtimeService.startProcessInstanceByKey("workAudit");
    }

//    @Bean
//    @Primary
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource activitiDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean
//    public SpringProcessEngineConfiguration springProcessEngineConfiguration(
//            PlatformTransactionManager transactionManager,
//            SpringAsyncExecutor springAsyncExecutor) throws IOException {
//
//        SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();
//        configuration.setDataSource(activitiDataSource());
//        configuration.setTransactionManager(transactionManager);
//        configuration.setAsyncExecutor(springAsyncExecutor);
//
//        return configuration;
//    }
}
