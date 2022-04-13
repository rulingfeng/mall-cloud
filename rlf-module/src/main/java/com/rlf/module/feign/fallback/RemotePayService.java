package com.rlf.module.feign.fallback;

import com.rlf.module.entity.Car;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import sun.security.util.SecurityConstants;

/**
 * @author: 茹凌丰
 * @date: 2022/4/13
 * @description:
 */
@FeignClient(value = "parking-pay", fallbackFactory = RemotePayServiceFallbackFactory.class)
public interface RemotePayService {

    @PostMapping("/pay")
    Object pay(@RequestBody Car car);
}
