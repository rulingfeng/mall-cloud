package com.rlf.module.feign.service;

import com.macro.mall.common.api.CommonResult;
import com.macro.mall.common.dto.PmsProductQueryParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "mall-admin",fallback = FeignFallbackService.class)
public interface FeignService {

    @GetMapping("/product/list")
    CommonResult getProductList(@RequestParam PmsProductQueryParam pmsProductQueryParam, @RequestParam Integer pageSize, @RequestParam Integer pageNum);
}
