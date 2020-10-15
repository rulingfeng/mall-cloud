package com.rlf.module.design.factory.abstractFactory.vehicleEntity;

import com.rlf.module.design.factory.abstractFactory.Vehicle;

/**
 * @author 茹凌丰
 * @Description: 林肯车 (美国人有的)
 * @date 2020/10/15-20:25
 */
public class LinKen extends Vehicle {
    @Override
    public void go() {
        System.out.println("LinKenCar go");
    }
}
