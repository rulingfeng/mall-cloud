package com.rlf.module.design.factory.abstractFactory;

import com.rlf.module.design.factory.abstractFactory.foodEntity.Steak;
import com.rlf.module.design.factory.abstractFactory.vehicleEntity.LinKen;
import com.rlf.module.design.factory.abstractFactory.weaponEntity.Pistol;

/**
 * @author 茹凌丰
 * @Description: 美国人的工厂
 * @date 2020/10/15-20:34
 */
public class AmericanFactory extends AbstractFactory{
    @Override
    public Food createFood() {
        return new Steak();
    }

    @Override
    public Vehicle createVehicle() {
        return new LinKen();
    }

    @Override
    public Weapon createWeapon() {
        return new Pistol();
    }
}
