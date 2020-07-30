package com.rlf.module.design.proxy;

import com.rlf.module.entity.People;
import com.rlf.module.entity.User;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author RU
 * @date 2020/7/30
 * @Desc
 */
public class UserProxy implements InvocationHandler {

    private Class target;


    public <T>T getProxy(Class<T> c)  {
        this.target = c;
        return (T) Proxy.newProxyInstance(
                c.getClassLoader()
                ,c.isInterface() ? new Class[]{c} : c.getInterfaces()
                ,this);
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("代理之前执行");
     //   if ( !target.isInterface() ){
            method.invoke(target.newInstance(),args);
   //     }
        System.out.println("代理之后执行");
        return "111";
    }


    public static void main(String[] args) {
        UserProxy userProxy = new UserProxy();
        People proxy = userProxy.getProxy(User.class);
        proxy.speak();




    }
}
