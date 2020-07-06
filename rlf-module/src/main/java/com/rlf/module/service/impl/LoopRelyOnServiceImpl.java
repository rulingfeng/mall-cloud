package com.rlf.module.service.impl;

import com.rlf.module.service.HystrixService;
import com.rlf.module.service.LoopRelyOnService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author RU
 * @date 2020/7/4
 * @Desc
 */
@Service
public class LoopRelyOnServiceImpl implements LoopRelyOnService {

    @Resource
    HystrixService hystrixService;
}
