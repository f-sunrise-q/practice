package com.yuyan.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.yuyan.service.HelloRemote;
import com.yuyan.service.HelloRemote2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 * </p>
 *
 * @author fangqin 2018/7/25 13:11
 * @version V1.0
 */
@RestController
public class TestController {
    @Autowired
    private HelloRemote helloRemote;

    @Autowired
    private HelloRemote2 helloRemote2;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/hello2")
    @HystrixCommand(fallbackMethod = "dealError")
    public String hello2(){
        return restTemplate.getForObject("http://provider/serviceProvider/sayHello", String.class);
    }

    private String dealError(){
        return "sorry, Hystrix deal error"+"!!!";
    }

    @RequestMapping("/hello")
    public String testFeignHystrix() {
        return helloRemote.sayHello();
    }

    @RequestMapping("/hello3")
    public String testFeignHystrix2() {
        return helloRemote2.sayHello();
    }

}
