package com.rlf.module.design.factory.factorymethod;

/**
 * @author 茹凌丰
 * @Description:
 * @date 2020/10/15-19:56
 */
public class VehicleFactory {

    public Car createCar() {
        //do something 生产过程
        return new Car();
    }

    public Plane createPlane() {
        //do something
        return new Plane();
    }
}
