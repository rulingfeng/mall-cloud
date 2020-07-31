package com.rlf.module.controller;


import com.macro.mall.common.api.CommonResult;
import com.macro.mall.common.dto.PmsProductQueryParam;
import com.rlf.module.annotation.token.TokenToMallUser;
import com.rlf.module.entity.User;
import com.rlf.module.feign.service.FeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.BeanUtil;
import util.SystemClock;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/module")
@Slf4j
public class TestController {

    @Resource
    FeignService feignService;

    @GetMapping("/test")
    public CommonResult test(@TokenToMallUser String user){
        log.info("进入到test接口中");
        System.out.println("user:"+user);
        return CommonResult.success("OK");
    }

    //远程调用mall-admin/product/list
    @GetMapping("/feign")
    public CommonResult domainFeign(){
        return feignService.getProductList(new PmsProductQueryParam(), 5, 1);
    }


}
