package com.rlf.module.design.strategy.map_if_else;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author RU
 * @date 2020/9/2
 * @Desc
 */

@Component
public class MapIfElse {

    private final static Map<String, Function<String, String>> checkResultDispatcher = new HashMap<>();

    @PostConstruct
    public  void checkResultDispatcherInit()  {
//        checkResultDispatcher.put("校验1", order -> String.format("对%s执行业务逻辑1", order));
//        checkResultDispatcher.put("校验2", order -> String.format("对%s执行业务逻辑2", order));
//        checkResultDispatcher.put("校验3", order -> String.format("对%s执行业务逻辑3", order));
//        Function<String, String> res = checkResultDispatcher.get("校验1");
//        System.out.println(res.apply("校验1")); //打印: 对校验1执行业务逻辑1
        checkResultDispatcher.put("wx", type -> this.wx(type));
        checkResultDispatcher.put("zfb", type ->  this.zfb(type));
        checkResultDispatcher.put("yhk", type ->  this.yhk(type));
//        Function<String, String> res = checkResultDispatcher.get("zfb");
//        res.apply("zfb付款");
    }

    public  String wx(String type){
        System.out.println(type);
        return type;
    }
    public  String zfb(String type){
        System.out.println(type);
        return type;
    }
    public  String yhk(String type){
        System.out.println(type);
        return type;
    }

    public static Map<String, Function<String, String>> getCheckResultDispatcher() {
        return checkResultDispatcher;
    }
}
