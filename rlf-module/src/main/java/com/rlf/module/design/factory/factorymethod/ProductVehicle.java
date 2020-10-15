package com.rlf.module.design.factory.factorymethod;

/**
 * @author 茹凌丰
 * @Description: 生产工厂
 * @date 2020/10/15-19:58
 */
public class ProductVehicle {
    public static void main(String[] args) {
        //简单工厂,可扩展性不好
        Car car = new VehicleFactory().createCar();
        Plane plane = new VehicleFactory().createPlane();
        car.go();
        plane.go();

        //比较好的一种写法 每个交通工具都对应一个生产工厂类
        Car car1 = new CarFactory().create();
        Plane plane1 = new PlaneFactory().create();
        car1.go();
        plane1.go();

    }
}
