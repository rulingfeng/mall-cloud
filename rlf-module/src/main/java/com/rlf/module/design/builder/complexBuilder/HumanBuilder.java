package com.rlf.module.design.builder.complexBuilder;

/**
 * @author 茹凌丰
 * @Description: 人类建造接口
 * @date 2020/10/15-20:56
 */
public interface HumanBuilder {
    HumanBuilder buildHead();
    HumanBuilder buildHand();
    HumanBuilder buildFoot();

    Human build();
}
