package com.example.disruptor_crud.firstexperience;

import com.lmax.disruptor.WorkHandler;

public class RingBufferOrderHandler implements WorkHandler<Order> {


    @Override
    public void onEvent(Order order) throws Exception {
        System.out.println(Thread.currentThread().getName() + " 消费者处理中:" + order.toString());
        order.setInfo("info" + order.getId());
        order.setPrice(Math.random());
    }
}
