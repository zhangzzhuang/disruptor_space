package com.basi.disruptor_ms.mq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqServerConfiguration implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    /**
     * mq服务器地址
     */
    @Value("${rocketmq.nameServer}")
    private String nameServer;

    /**
     * 生产者的组名
     */
    @Value("${rocketmq.producer.producerGroup}")
    private String producerGroup;

    /**
     *
     */
    @Value("${rocketmq.topic}")
    private String topic;

    /**
     *
     */
    @Value("${rocketmq.tag}")
    private String tag;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    @Bean
    public MessageSender requestMessageSender(){

        DefaultMQProducer producer = new DefaultMQProducer(producerGroup);
        producer.setNamesrvAddr(nameServer);
        producer.setSendMessageWithVIPChannel(false);
        producer.setVipChannelEnabled(false);
        MessageSenderImpl messageSender = new MessageSenderImpl(producer,topic,tag);
        return messageSender;
    }

}
