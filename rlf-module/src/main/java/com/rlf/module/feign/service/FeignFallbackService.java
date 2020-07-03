package com.rlf.module.feign.service;

import com.macro.mall.common.api.CommonResult;
import com.macro.mall.common.dto.PmsProductQueryParam;
import org.springframework.stereotype.Service;

//服务降级   Hystrix功能     yml文件配置feign:hystrix:enabled: true
@Service
public class FeignFallbackService implements FeignService {

    @Override
    public CommonResult getProductList(PmsProductQueryParam pmsProductQueryParam, Integer pageSize, Integer pageNum) {
        System.out.println("进到服务降级");
        return CommonResult.failed("服务降级");
    }
}
