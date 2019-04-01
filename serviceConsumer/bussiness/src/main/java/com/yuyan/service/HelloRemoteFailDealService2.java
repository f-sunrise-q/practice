package com.yuyan.service;

import org.springframework.stereotype.Component;

@Component
public class HelloRemoteFailDealService2 implements HelloRemote2{
    @Override
    public String sayHello() {
        return "Sorry, Feign hystrix!!!";
    }
}
