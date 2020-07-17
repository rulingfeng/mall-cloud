package com.rlf.module.design.Observer;

import lombok.Builder;
import lombok.Data;

import java.util.Observable;
import java.util.Observer;

/**
 * @author RU
 * @date 2020/7/17
 * @Desc  观察者类     如果有一个被观察者有情况  这里就会马上监听到   (多)
 */
@Data
@Builder
public class ObserverUser implements Observer {

    private String name;

    @Override
    public void update(Observable o, Object arg) {
        //监听到后 todo something
        System.out.println("🕵观察者监听到: " + name + ",内容为: "+arg);
    }
}
