package com.basi.disruptor_ms.mq;

import com.alibaba.fastjson.JSON;
import com.basi.disruptor_ms.entity.RequestDto;
import com.basi.disruptor_ms.entity.StartupOrderConstants;
import com.basi.disruptor_ms.request.RequestDtoEventProducer;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.UtilAll;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;


@Component
public class MqClientConfiguration implements InitializingBean, SmartLifecycle {

    private static final Logger logger = LoggerFactory.getLogger(MqClientConfiguration.class);

    /**
     * mq服务器地址
     */
    @Value("${rocketmq.nameServer}")
    private String nameServer;

    /**
     * 生产者的组名
     */
    @Value("${rocketmq.consumer.consumerGroup}")
    private String consumerGroup;

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

    private DefaultMQPushConsumer consumer;

    @Autowired
    private RequestDtoEventProducer requestDtoEventProducer;

    @PostConstruct
    public void initConcurrentlyConsumer() {
        try {
            consumer = new DefaultMQPushConsumer(consumerGroup);
            consumer.setNamesrvAddr(nameServer);

            /**
             * 设置Consumer第一次启动是从当前时间开始消费<br>
             */
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_TIMESTAMP);
            consumer.setConsumeTimestamp(UtilAll.timeMillisToHumanString3(System.currentTimeMillis()));

            consumer.subscribe(topic, tag);

            consumer.setMaxReconsumeTimes(4);
            consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
                if (CollectionUtils.isEmpty(msgs)) {
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }

                MessageExt message = msgs.get(0);
                final LocalDateTime now = LocalDateTime.now();
                String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);

                logger.info("当前时间:{},messageId:{},topic:{},messageBody:{}", now, message.getMsgId(), message.getTopic(), messageBody);
                RequestDto requestDto = JSON.parseObject(messageBody, RequestDto.class);
                try {
                    if (requestDto.getItemId().equals(673125L)){
                        int a = 1 / 0;
                    }
                    requestDtoEventProducer.onData(requestDto);
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                } catch (Exception e){
                    logger.info("模拟消费者失败，参数:{}",messageBody);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }

            });

            logger.info("MqClient Properties start------");
        } catch (Exception e) {
            logger.error("MqClient Properties error : [{}]", ExceptionUtils.getStackTrace(e));
        }
    }


//    @PostConstruct
//    public void initDLQConsumer() {
//        try {
//            consumer = new DefaultMQPushConsumer("dlq_ms_consumer_prod");
//            consumer.setNamesrvAddr(nameServer);
//
//            /**
//             * 设置Consumer第一次启动是从当前时间开始消费<br>
//             */
//            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_TIMESTAMP);
//            consumer.setConsumeTimestamp(UtilAll.timeMillisToHumanString3(System.currentTimeMillis()));
//
//            consumer.subscribe("%DLQ%ms_consumer_prod", tag);
//
//            consumer.setMaxReconsumeTimes(4);
//            consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
//                if (CollectionUtils.isEmpty(msgs)) {
//                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//                }
//
//                MessageExt message = msgs.get(0);
//                final LocalDateTime now = LocalDateTime.now();
//                String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
//
//                logger.info("当前时间:{},messageId:{},topic:{},messageBody:{}", now, message.getMsgId(), message.getTopic(), messageBody);
//                RequestDto requestDto = JSON.parseObject(messageBody, RequestDto.class);
//                try {
//                    if (requestDto.getItemId().equals(673125L)){
//                        int a = 1 / 0;
//                    }
//                    requestDtoEventProducer.onData(requestDto);
//                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//                } catch (Exception e){
//                    logger.info("模拟消费者失败，参数:{}",messageBody);
//                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
//                }
//
//            });
//
//            logger.info("DLQ MqClient Properties start------");
//        } catch (Exception e) {
//            logger.error("DLQ MqClient Properties error : [{}]", ExceptionUtils.getStackTrace(e));
//        }
//    }



        @Override
    public void afterPropertiesSet() {
        try {
            consumer = new DefaultMQPushConsumer(consumerGroup);
            consumer.setNamesrvAddr(nameServer);

            // 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费<br>
            // 如果非第一次启动，那么按照上次消费的位置继续消费
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

            consumer.subscribe(topic, tag);

            consumer.setConsumeMessageBatchMaxSize(500);
            consumer.setMaxReconsumeTimes(8);
            consumer.registerMessageListener((MessageListenerOrderly) (list, context) -> {
                for (MessageExt msg : list) {

                    String body = new String(msg.getBody(), StandardCharsets.UTF_8);
                    RequestDto requestDto = JSON.parseObject(body, RequestDto.class);
                    logger.info("RECEIVE_MSG ：【{}】", requestDto);
                    requestDtoEventProducer.onData(requestDto);
                }
                return ConsumeOrderlyStatus.SUCCESS;
            });

            logger.info("MqClient Properties ------");
        } catch (Exception e) {
            logger.error("MqClient Properties error : [{}]", ExceptionUtils.getStackTrace(e));
        }

    }


    private volatile boolean running = false;

    private final Object monitor = new Object();

    @Override
    public boolean isAutoStartup() {
        return SmartLifecycle.super.isAutoStartup();
    }

    @Override
    public void start() {

        synchronized (this.monitor) {
            if (!this.running) {
                try {
                    this.consumer.start();
                } catch (MQClientException e) {
                    logger.error("MqClient start失败:[{}]", e.getMessage());
                }
            }
            this.running = true;
        }
    }


    @Override
    public void stop(Runnable callback) {
        logger.info("MQ_CLIENT_STOP ---------");
        this.consumer.shutdown();
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

        logger.info("MQ_CLIENT_START ---------");
        return StartupOrderConstants.MQ_CLIENT_START;
    }


}
