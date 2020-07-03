package com.rlf.module.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.macro.mall.common.api.CommonResult;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import com.rlf.module.service.HystrixService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author RU
 * @date 2020/7/3
 * @Desc 熔断器相关 服务降级  及  缓存
 */
@Service
@Slf4j
public class HystrixServiceImpl implements HystrixService {
    @Resource
    RestTemplate restTemplate;

    String HOST_MALL_ADMIN = "http://localhost:8201/mall-admin";

    HashMap<String , Object> map = new HashMap<>();

    {
        map.put("1",1);
        map.put("2",2);
        map.put("3",3);
    }

    @Override
    @HystrixCommand(fallbackMethod = "bbb")
    @CacheResult(cacheKeyMethod = "getCacheKey")   //需要配置 HystrixRequestContextFilter类
    //@CacheResult注解只在一个请求中连续调用多次会生效走缓存, 每次不同请求都不会走缓存  没点逼用
    public Object getList(Long id) {
        System.out.println("进入getList方法"+id);
        String url = HOST_MALL_ADMIN + "/product/list";
        Map<String, Object> params = new HashMap<>();
        params.put("pageSize", 5);
        params.put("pageNum", 1);
        ResponseEntity<CommonResult> responseEntity = restTemplate.getForEntity(url, CommonResult.class, params);
        return responseEntity.getBody();
    }

    //服务降低的指定方法
    public Object bbb(@PathVariable Long id){
        return CommonResult.failed("HystrixCommand-bbb注解进到降级,id="+id+",name=");
    }

    /**
     * 为缓存生成key的方法
     */
    public String getCacheKey(Long id) {
        return String.valueOf(id);
    }

    //---------------------------------


    @Override
    @HystrixCollapser(batchMethod = "getUserByIds",collapserProperties = {
            @HystrixProperty(name = "timerDelayInMilliseconds", value = "100")
    })
    public Future<Object> hystrixCollapser(Long id) {
        return new AsyncResult<Object>() {
            @Override
            public Object invoke(){
                return map.get(id.toString());
            }
        };
    }

    @HystrixCommand
    public List<Object> getUserByIds(List<Long> ids) {
        log.info("getUserByIds:{}", ids);
        return ids.stream().map(i -> map.get(i.toString())).collect(Collectors.toList());
    }
}
