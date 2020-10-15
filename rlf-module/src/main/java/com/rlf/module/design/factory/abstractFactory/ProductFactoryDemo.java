package com.rlf.module.design.factory.abstractFactory;

/**
 * @author 茹凌丰
 * @Description: 生产工厂示例
 * @date 2020/10/15-20:36
 */
public class ProductFactoryDemo {
    public static void main(String[] args) {
        AbstractFactory chineseFactory = new ChineseFactory();
        chineseFactory.createFood().eat();
        chineseFactory.createVehicle().go();
        chineseFactory.createWeapon().kill();

        AbstractFactory americanFactory = new AmericanFactory();
        americanFactory.createFood().eat();
        americanFactory.createVehicle().go();
        americanFactory.createWeapon().kill();


    }
}
