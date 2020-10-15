package com.rlf.module.design.builder.complexBuilder;

/**
 * @author 茹凌丰
 * @Description: 复杂构造器演示
 * @date 2020/10/15-21:01
 */
public class ComplexBuilderDemo {
    public static void main(String[] args) {
        //用于构造器中,有很多复杂的属性 起码4,5个以上,每个属性构造也很复杂
        Human build = new ComplexHumanBuilder().buildFoot().buildHand().buildHead().build();

    }
}
