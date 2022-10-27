package com.example.disruptor_crud.firstexperience;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.Executors;

public class Main {


    public static void main(String[] args) throws InterruptedException {

        //创建订单工厂
        OrderFactory orderFactory = new OrderFactory();
        //ringbuffer的大小
        int RING_BUFFER_SIZE = 1024;
        //创建disruptor
//        Disruptor<Order> disruptor = new Disruptor<Order>(orderFactory,RING_BUFFER_SIZE, Executors.defaultThreadFactory());
        Disruptor<Order> disruptor = new Disruptor<Order>(orderFactory,RING_BUFFER_SIZE, Executors.defaultThreadFactory(), ProducerType.SINGLE,new YieldingWaitStrategy());

        //设置事件处理器 即消费者
        disruptor.handleEventsWith(new OrderHandler());
        disruptor.start();

        RingBuffer<Order> ringBuffer = disruptor.getRingBuffer();

        for (int i = 0; i < 100; i++) {
            long next = ringBuffer.next();
            Order order = ringBuffer.get(next);
            order.setId(i);
            ringBuffer.publish(next);
            System.out.println(Thread.currentThread().getName() + " 生产者发布一条数据:" + next + " 订单ID：" + i);

        }

        Thread.sleep(10000);

        disruptor.shutdown();
    }
}
