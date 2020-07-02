package com.rlf.module.design.singleton;

/**
 * @Description: 枚举单例模式
 * @Author: RU
 * @Date: 2020/4/18 17:16
 */
public class SingletonInstants {
    private SingletonInstants(){}

    static enum SingletonInstantsEnum{
        INSTANCE;
        private SingletonInstants singletonInstants;

        private SingletonInstantsEnum(){
            singletonInstants = new SingletonInstants();
        }
        public SingletonInstants getInstance(){
            return singletonInstants;
        }
    }

    public static SingletonInstants getInstance(){
        return SingletonInstantsEnum.INSTANCE.getInstance();
    }
}
