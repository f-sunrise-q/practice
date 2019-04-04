package com.yuyan.consul.consul;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/getStr")
    public String getTestStr(){
        return "hello";
    }
}
