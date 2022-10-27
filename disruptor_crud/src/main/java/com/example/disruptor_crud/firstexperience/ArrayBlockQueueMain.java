package com.example.disruptor_crud.firstexperience;

import java.util.concurrent.ArrayBlockingQueue;

public class ArrayBlockQueueMain {

    private static boolean flag = true;
    ArrayBlockingQueue<Integer> arrayBlockingQueue = new ArrayBlockingQueue(16);

    public static void main(String[] args) throws InterruptedException {

        long l = System.currentTimeMillis();
        ArrayBlockQueueMain arrayBlockQueueMain = new ArrayBlockQueueMain();
        new Thread(() -> {
            for (int i = 0; i < 10000000; i++) {
                try {
                    arrayBlockQueueMain.arrayBlockingQueue.put(i);
                    System.out.println(Thread.currentThread().getName() + "----" + "发布用户" + i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "producer").start();

        new Thread(() -> {
            while (flag) {
                try {
                    Integer take = arrayBlockQueueMain.arrayBlockingQueue.take();
                    System.out.println(Thread.currentThread().getName() + "----" + "消费用户" + take);
                    if (take.equals(9999999)) {
                        flag = false;
                        System.out.println("耗时:" + (System.currentTimeMillis() - l));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }, "consumer").start();


    }
}
