package com.rlf.module.design.factory.abstractFactory;

/**
 * @author 茹凌丰
 * @Description: 产品一族的抽象工厂
 *      有中国人 美国人 德国人
 *      每一种人吃不同的食物,坐不同的车,持不同的武器
 *
 *
 *      形容词用接口,名词用抽象类
 * @date 2020/10/15-20:13
 */
public abstract class AbstractFactory {
    public abstract Food createFood();
    public abstract Vehicle createVehicle();
    public abstract Weapon createWeapon();
}
