package com.rlf.module.design.factory.abstractFactory.vehicleEntity;

import com.rlf.module.design.factory.abstractFactory.Vehicle;

/**
 * @author 茹凌丰
 * @Description: 红旗车 (中国人有的)
 * @date 2020/10/15-20:19
 */
public class HongQi extends Vehicle {
    @Override
    public void go(){
        System.out.println("HongQiCar go");
    }
}
