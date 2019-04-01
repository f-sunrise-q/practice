package com.yuyan.service;

import org.springframework.stereotype.Component;

@Component
public class HelloRemoteFailDealService implements HelloRemote{
    @Override
    public String sayHello() {
        return "Sorry, Feign hystrix!!!";
    }
}
