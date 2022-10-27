package com.basi.disruptor_ms.mq;

import com.basi.disruptor_ms.entity.StartupOrderConstants;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;


import java.nio.charset.StandardCharsets;

public class MessageSenderImpl implements MessageSender, SmartLifecycle {

    private static final Logger logger = LoggerFactory.getLogger(MessageSenderImpl.class);

    private DefaultMQProducer producer;

    private String topic;

    private String tag;

    public MessageSenderImpl(DefaultMQProducer producer, String topic, String tag) {
        this.producer = producer;
        this.topic = topic;
        this.tag = tag;
    }

    @Override
    public void sendMessage(String payload,String key) throws Exception {
        Message msg = new Message(topic, tag, payload.getBytes(StandardCharsets.UTF_8));
        msg.setKeys(key);
        producer.sendOneway(msg);
    }

    private volatile boolean running = false;

    private final Object monitor = new Object();

    @Override
    public boolean isAutoStartup() {
        return SmartLifecycle.super.isAutoStartup();
    }

    @Override
    public void start() {

        synchronized (this.monitor){
            if (!this.running){
                try {
                    this.producer.start();
                } catch (MQClientException e) {
                    logger.error("MqServer start 失败:[{}]", e.getMessage());
                }
            }
            this.running = true;
        }
    }

    @Override
    public void stop(Runnable callback) {
        logger.info("MQ_SERVER_STOP ---------");
        this.producer.shutdown();
        this.stop();
        callback.run();
    }

    @Override
    public void stop() {
        this.running = false;
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    @Override
    public int getPhase() {

        logger.info("MQ_SERVER_START ---------");
        return StartupOrderConstants.MQ_SERVER_START;
    }
}
