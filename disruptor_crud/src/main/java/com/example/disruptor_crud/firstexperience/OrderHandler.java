package com.example.disruptor_crud.firstexperience;

import com.lmax.disruptor.EventHandler;


public class OrderHandler implements EventHandler<Order> {

    @Override
    public void onEvent(Order order, long l, boolean b) throws Exception {

        System.out.println(Thread.currentThread().getName() + " 消费者处理中:" + l);
        order.setInfo("info" + order.getId());
        order.setPrice(Math.random());
    }


}
