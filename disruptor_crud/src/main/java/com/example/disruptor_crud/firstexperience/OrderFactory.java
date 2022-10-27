package com.example.disruptor_crud.firstexperience;

import com.lmax.disruptor.EventFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class OrderFactory implements EventFactory {

    public static AtomicInteger atomicInteger = new AtomicInteger();

    @Override
    public Object newInstance() {
        int andIncrement = atomicInteger.getAndIncrement();
        System.out.println("OrderFactory.newInstance==" + andIncrement);
        return new Order();
    }
}
