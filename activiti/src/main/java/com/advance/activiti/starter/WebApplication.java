package com.advance.activiti.starter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {"com.advance"})
@ComponentScan({"com.advance"})
@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
public class WebApplication {

    private static final Logger log = LoggerFactory.getLogger(WebApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(WebApplication.class, args);

        log.warn("system start success");
    }
}
