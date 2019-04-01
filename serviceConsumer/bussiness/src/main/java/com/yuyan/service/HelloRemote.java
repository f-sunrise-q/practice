package com.yuyan.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * <p>
 * </p>
 *
 * @author fangqin 2018/7/25 13:09
 * @version V1.0
 */
@FeignClient(name = "provider", fallback = HelloRemoteFailDealService.class, configuration = DisableFeignHystrixConfiguration.class)
public interface HelloRemote {
    @RequestMapping(method = RequestMethod.GET, value = "/serviceProvider/sayHello")
    String sayHello();
}
