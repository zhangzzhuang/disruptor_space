package com.example.disruptor_crud.firstexperience;

import com.lmax.disruptor.IgnoreExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkerPool;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class RingBufferMain {

    public static void main(String[] args) throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        RingBuffer<Order> ringBuffer = RingBuffer.create(ProducerType.SINGLE,new OrderFactory(),1024,new YieldingWaitStrategy());
        WorkerPool<Order> workerPool = new WorkerPool<Order>(ringBuffer,ringBuffer.newBarrier(),new IgnoreExceptionHandler(),new RingBufferOrderHandler());
        workerPool.start(executorService);
        for (int i = 0; i < 30; i++) {
            long next = ringBuffer.next();
            Order order = ringBuffer.get(next);
            order.setId(i);
            ringBuffer.publish(next);
            System.out.println(Thread.currentThread().getName() + " 生产者发布一条数据:" + next + " 订单ID：" + i);
        }
        Thread.sleep(3000);
        workerPool.halt();
        executorService.shutdown();
    }
}
