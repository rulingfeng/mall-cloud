package com.rlf.module.design.factory.abstractFactory;

import com.rlf.module.design.factory.abstractFactory.foodEntity.Rice;
import com.rlf.module.design.factory.abstractFactory.vehicleEntity.HongQi;
import com.rlf.module.design.factory.abstractFactory.weaponEntity.Knife;

/**
 * @author 茹凌丰
 * @Description: 中国人的工厂
 * @date 2020/10/15-20:32
 */
public class ChineseFactory extends AbstractFactory {
    @Override
    public Food createFood() {
        return new Rice();
    }

    @Override
    public Vehicle createVehicle() {
        return new HongQi();
    }

    @Override
    public Weapon createWeapon() {
        return new Knife();
    }
}
