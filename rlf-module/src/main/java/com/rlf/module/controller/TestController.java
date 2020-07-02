package com.rlf.module.controller;


import com.macro.mall.common.api.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/module")
@Slf4j
public class TestController {

    @GetMapping("/test")
    public CommonResult test(){
        log.info("进入到test接口中");
        return CommonResult.success("OK");
    }
}
