package com.basi.disruptor_ms.util;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.context.ApplicationContext;

/**
 * isAssignableFrom()方法是从类继承的角度去判断,判断是否为某个类的父类
 */
public abstract class BeanRegisterUtils {

    private BeanRegisterUtils() {

    }

    public static void registerSingleton(ApplicationContext applicationContext,String beanName,Object singletonObject){

        AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
        if (!SingletonBeanRegistry.class.isAssignableFrom(beanFactory.getClass())){
            throw new IllegalArgumentException("ApplicationContext: "+applicationContext.getClass().toString()+" doesn't implements SingletonBeanRegistry , cannot register at runtime" );
        }


        SingletonBeanRegistry beanDefinitionRegistry = (SingletonBeanRegistry) beanFactory;
        beanDefinitionRegistry.registerSingleton(beanName,singletonObject);
    }

}
