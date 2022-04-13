package com.rlf.module.feign.fallback;

import com.rlf.module.entity.Car;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author: 茹凌丰
 * @date: 2022/4/13
 * @description:
 */
@Component
public class RemotePayServiceFallbackFactory implements FallbackFactory<RemotePayService> {


    @Override
    public RemotePayService create(Throwable throwable) {
        RemotePayServiceFallbackImpl remotePayServiceFallback = new RemotePayServiceFallbackImpl();
        remotePayServiceFallback.setCause(throwable);
        return remotePayServiceFallback;
    }
}
