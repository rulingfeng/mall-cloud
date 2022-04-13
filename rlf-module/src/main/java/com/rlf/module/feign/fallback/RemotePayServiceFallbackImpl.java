package com.rlf.module.feign.fallback;


import com.rlf.module.entity.Car;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * @author: 茹凌丰
 * @date: 2022/4/13
 * @description:
 */
@Slf4j
@Component
public class RemotePayServiceFallbackImpl implements RemotePayService {
    @Setter
    Throwable cause;

    @Override
    public Object pay(Car car) {
        log.error("== feign 调用计费接口失败{}", cause);
        return null;
    }
}
