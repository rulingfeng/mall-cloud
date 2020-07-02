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
public class StrategyZFBServiceImpl implements StrategyService, InitializingBean {
    @Autowired
    ApplicationContext applicationContext;

    @Override
    public void test() {
        //todo something        (zfb专属的逻辑)
        System.out.println("This is ZFB method");
    }



    @Override
    public void afterPropertiesSet()  {
        StrategyObject.putObj("zfb",applicationContext.getBean(StrategyZFBServiceImpl.class));
    }
}
