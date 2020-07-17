package com.rlf.module.design.Observer;

/**
 * @author RU
 * @date 2020/7/17
 * @Desc
 */
public class ObservedImpl {
    public static void main(String[] args) {
        ObservableListener listener = new ObservableListener();

        ObserverUser one = ObserverUser.builder().name("一").build();
        ObserverUser two = ObserverUser.builder().name("二").build();
        ObserverUser third = ObserverUser.builder().name("三").build();

        listener.addObserver(one);
        listener.addObserver(two);
        listener.addObserver(third);

        listener.publishEvent("发送消息1111");
        listener.deleteObserver(one);
        System.out.println("-----------------------------");
        listener.publishEvent("发送消息2222");

    }
}
