package com.rlf.module.threadDispatch;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: 茹凌丰
 * @date: 2022/6/2
 * @description:
 */
public class FunctionHandleUtil {

    public static <T,R>R hanleFunction(T value, Function<T,R> function)  {
        return function.apply(value);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        List<String> names = Lists.newArrayList("张三","李四","王五");
        Function<String,String> function = (val) -> val + "你好";
        List<String> collect = names.stream().map(i -> FunctionHandleUtil.hanleFunction(i, function)).collect(Collectors.toList());
        collect.forEach(System.out::println);

    }
}
