package com.rlf.module.design.strategy;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: RU
 * @Date: 2020/4/18 15:55
 */
@RestController
@Slf4j
@RequestMapping("/" +
        "")
public class StrategyController {

    @GetMapping("/aaa")
    public String aaaa(@RequestParam("type") String type){
        StrategyService strategyService = StrategyObject.getMap(type);
        strategyService.test();
        strategyService.init();
        return null;
    }

}
