package com.rlf.module.design.Observer;

import lombok.Data;

import java.util.Observable;

/**
 * @author RU
 * @date 2020/7/17
 * @Desc
 */

public class ObservableListener extends Observable {

    public void publishEvent(String info){

        //标识这个Observable对象已经改变了，更具体来将就是把Observable中属性changed置为true
        setChanged();
        //在通知所有观察者之前，需要判断Observable中属性changed是否为true，如若不为则不会发出通知。
        notifyObservers(info);

    }
}
