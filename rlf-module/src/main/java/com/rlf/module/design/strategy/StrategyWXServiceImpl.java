package com.rlf.module.design.strategy;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description:
 * @Author: RU
 * @Date: 2020/4/18 15:49
 */
@Service
public class StrategyWXServiceImpl implements StrategyService , InitializingBean {
    @Autowired
    ApplicationContext applicationContext;


    @Override
    public void test() {
        //todo something      (wx专属的逻辑)
        System.out.println("This is WX method");
    }

    @Override
    public void afterPropertiesSet()  {
        StrategyObject.putObj("wx",applicationContext.getBean(StrategyWXServiceImpl.class));
    }
}
