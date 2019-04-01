package com.yuyan.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import zipkin2.server.internal.EnableZipkinServer;

@SpringBootApplication(scanBasePackages = {"com.yuyan"})
@ComponentScan({"com.yuyan"})
@EnableZipkinServer
@EnableEurekaClient
@EnableDiscoveryClient
public class StarterApplication {

	public static void main(String[] args) {
		SpringApplication.run(StarterApplication.class, args);
	}
}
