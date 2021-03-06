package com.yuyan.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@SpringBootApplication(scanBasePackages = {"com.yuyan"})
@ComponentScan({"com.yuyan"})
@EnableEurekaServer
public class StarterApplication extends WebSecurityConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(StarterApplication.class, args);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Configure HttpSecurity as needed (e.g. enable http basic).
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
		http.csrf().disable();
		//注意：为了可以使用 http://${user}:${password}@${host}:${port}/eureka/ 这种方式登录,所以必须是httpBasic,
		// 如果是form方式,不能使用url格式登录
		http.authorizeRequests().anyRequest().authenticated().and().httpBasic();
	}
}
