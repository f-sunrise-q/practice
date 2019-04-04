package com.yuyan.consul.consul;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.discovery.HeartbeatProperties;
import org.springframework.cloud.consul.serviceregistry.ConsulAutoRegistration;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistrationCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class ConsulApplication {

	public static void main(String[] args) {
//		SpringApplication.run(ConsulApplication.class, args);

		new SpringApplicationBuilder(ConsulApplication.class).web(WebApplicationType.SERVLET).run(args);
	}

	@Bean
	public ConsulAutoRegistration consulRegistration(AutoServiceRegistrationProperties autoServiceRegistrationProperties,
													 ConsulDiscoveryProperties properties, ApplicationContext applicationContext,
													 ObjectProvider<List<ConsulRegistrationCustomizer>> registrationCustomizers, HeartbeatProperties heartbeatProperties) {
		properties.setHostname("localhost");
		String appName = applicationContext.getEnvironment().getProperty("spring.application.name");
		String port = applicationContext.getEnvironment().getProperty("server.port");
		properties.setInstanceId(appName + "-" + port + ":" + "localhost");
		return ConsulAutoRegistration.registration(autoServiceRegistrationProperties, properties,
				applicationContext, registrationCustomizers.getIfAvailable(), heartbeatProperties);
	}

}
