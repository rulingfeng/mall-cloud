package com.rlf.module.design.strategy.enum_if_else;

/**
 * @author RU
 * @date 2020/8/21
 * @Desc
 */
public class EnumIfElseTest {

    public static void main(String[] args) {
        EnumIfElse.getDesciptionByCode(1).pay();
        String refund = EnumIfElse.getDesciptionByCode(2).refund();
        System.out.println(refund);
    }
}
