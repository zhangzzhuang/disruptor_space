package com.example.disruptor_crud.firstexperience;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DisruptorMain {


    static final AtomicInteger poolNumber = new AtomicInteger(1);

    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        EventTranslatorOneArg<Element, Object> TRANSLATOR = (element, sequence, value) -> element.setValue(value);

        // RingBuffer生产工厂,初始化RingBuffer的时候使用
        EventFactory<Element> factory = () -> new Element();

        // 指定RingBuffer的大小
        int bufferSize = 16;

        //生产者的线程工厂
        ThreadFactory threadFactory = r -> new Thread(r, "Disruptor_Thread_" + poolNumber.getAndIncrement());

        // 阻塞策略
        BlockingWaitStrategy strategy = new BlockingWaitStrategy();

        // 创建disruptor，采用单生产者模式
        Disruptor<Element> disruptor = new Disruptor<>(factory, bufferSize, threadFactory, ProducerType.SINGLE, strategy);

        // 处理Event的handler
        EventHandler<Element> handler = (element, l, b) -> {
            System.out.println(Thread.currentThread().getName() + " 消费者消费一条数据" + " 订单ID：" + element.getValue() +
                    "------" + System.currentTimeMillis());
            if (System.currentTimeMillis() % 7 == 0 && Thread.currentThread().getName().equals("Disruptor_Thread_2")) {
                throw new RuntimeException();
            }
        };

        // 设置EventHandler
        disruptor.handleEventsWith(handler);

        disruptor.setDefaultExceptionHandler(new EventExceptionHandler());
        // 启动disruptor的线程
        disruptor.start();

        RingBuffer<Element> ringBuffer = disruptor.getRingBuffer();

        for (int i = 0; i < 10000000 ; i++) {
            Element element = new Element();
            element.setValue(i);

            ringBuffer.publishEvent(TRANSLATOR, element);
//            // 获取下一个可用位置的下标
//            long next = ringBuffer.next();
//            Element element = ringBuffer.get(next);
//            element.setValue(i);
//            ringBuffer.publish(next);
            System.out.println(Thread.currentThread().getName() + " 生产者发布一条数据:" + ringBuffer.getCursor() + " 订单ID：" + i + "---" + System.currentTimeMillis());

        }

        System.out.println("耗时:" + (System.currentTimeMillis() - start));
    }


}

// 队列中的元素
class Element {

    private Object value;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}

class EventExceptionHandler implements ExceptionHandler<Element> {


    /**
     * 处理中
     *
     * @param throwable
     * @param l
     * @param element
     */
    @Override
    public void handleEventException(Throwable throwable, long l, Element element) {
        System.out.println("Exception processing |" + element.getValue() + "|" + Thread.currentThread().getName());
    }

    @Override
    public void handleOnStartException(Throwable throwable) {
        System.out.println("Exception during onStart......" + throwable.getMessage());
    }

    @Override
    public void handleOnShutdownException(Throwable throwable) {
        System.out.println("Exception during onShutdown()" + throwable.getMessage());
    }
}
