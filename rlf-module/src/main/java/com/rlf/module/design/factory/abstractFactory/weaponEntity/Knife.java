package com.rlf.module.design.factory.abstractFactory.weaponEntity;

import com.rlf.module.design.factory.abstractFactory.Weapon;

/**
 * @author 茹凌丰
 * @Description: 刀 (中国人拿的)
 * @date 2020/10/15-20:29
 */
public class Knife extends Weapon {
    @Override
    public void kill() {
        System.out.println("Knife kill");
    }
}
