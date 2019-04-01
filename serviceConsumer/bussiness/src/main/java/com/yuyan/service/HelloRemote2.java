package com.yuyan.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "provider", fallback = HelloRemoteFailDealService2.class)
public interface HelloRemote2 {
    @RequestMapping(method = RequestMethod.GET, value = "/serviceProvider/sayHello")
    String sayHello();
}