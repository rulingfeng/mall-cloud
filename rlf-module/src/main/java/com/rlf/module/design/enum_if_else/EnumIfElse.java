package com.rlf.module.design.enum_if_else;

import lombok.Data;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author RU
 * @date 2020/8/21
 * @Desc
 */
@Getter
public enum EnumIfElse implements EnumIfElseUtil{
    WX(1,"微信"){
        @Override
        public void pay() {
            System.out.println("WX pay");
        }

        @Override
        public String refund() {
            System.out.println("WX refund");
            return "WX refund";
        }
    },
    APP(2,"APP"){
        @Override
        public void pay() {
            System.out.println("APP pay");
        }

        @Override
        public String refund() {
            System.out.println("APP refund");
            return "APP refund";
        }
    },
    ZFB(3,"支付宝"){
        @Override
        public void pay() {
            System.out.println("ZFB pay");
        }

        @Override
        public String refund() {
            System.out.println("ZFB refund");
            return "ZFB refund";
        }
    };

    private Integer code;
    private String desc;

    EnumIfElse(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static EnumIfElse getDesciptionByCode(Integer code){
        if(Objects.isNull(code)){
            return null;
        }
        return Arrays.stream(EnumIfElse.values())
                .filter(i -> i.getCode().equals(code)).findFirst().orElse(null);
    }


}
