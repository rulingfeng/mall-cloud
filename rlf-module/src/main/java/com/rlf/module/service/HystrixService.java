package com.rlf.module.service;

import java.util.concurrent.Future;

/**
 * @author RU
 * @date 2020/7/3
 * @Desc
 */
public interface HystrixService {

    Object getList(Long id);

    Future<Object> hystrixCollapser(Long id);
}
