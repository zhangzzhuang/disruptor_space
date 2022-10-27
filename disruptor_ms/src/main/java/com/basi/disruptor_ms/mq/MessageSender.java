package com.basi.disruptor_ms.mq;

public interface MessageSender {

    /**
     * 发送rocketmq消息
     *
     * @param payload 消息主体
     * @throws Exception
     */
    void sendMessage(String payload,String key) throws Exception;
}
