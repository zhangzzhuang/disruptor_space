package com.basi.disruptor_ms.config;

import com.basi.disruptor_ms.command.CommandDispatcher;
import com.basi.disruptor_ms.entity.StartupOrderConstants;
import com.basi.disruptor_ms.handler.*;
import com.basi.disruptor_ms.lifecycle.DisruptorLifeCycleContainer;
import com.basi.disruptor_ms.memdb.ItemRepository;
import com.basi.disruptor_ms.request.RequestDtoEvent;
import com.basi.disruptor_ms.request.RequestDtoEventFactory;
import com.basi.disruptor_ms.request.RequestDtoEventProducer;
import com.basi.disruptor_ms.util.BeanRegisterUtils;
import com.lmax.disruptor.dsl.Disruptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

@Configuration
@EnableConfigurationProperties({RequestDisruptorConfiguration.DisruptorProperties.class})
public class RequestDisruptorConfiguration implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(RequestDisruptorConfiguration.class);

    private ApplicationContext applicationContext;

    @Autowired
    private DisruptorProperties disruptorProperties;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean
    public CommandDispatcher commandDispatcher() {

        return new CommandDispatcher();
    }

    @Bean
    public RequestDtoEventProducer requestDtoEventProducer() {

        logger.info("Configure RequestDtoEventProducer......");

        RequestDtoEventDBHandler requestDtoEventDBHandler = new RequestDtoEventDBHandler();
        requestDtoEventDBHandler.setCommandDispatcher(commandDispatcher());

        RequestDtoEventOutputHandler requestDtoEventOutputHandler = new RequestDtoEventOutputHandler();

        Disruptor<RequestDtoEvent> disruptor = new Disruptor<>(RequestDtoEvent::new,
                disruptorProperties.getJvmQueueSize(),
                Executors.defaultThreadFactory());

        disruptor.handleEventsWith(requestDtoEventBusinessHandler())
                .then(requestDtoEventOutputHandler, requestDtoEventDBHandler)
                .then(new RequestDtoEventGCHandler());

        // disruptor 的异常处理是这样的,
        // 不论这种形式 A->B, 还是这种形式 A,B->C,D, 只有抛出异常的那个handler会中断执行
        disruptor.setDefaultExceptionHandler(new RequestDtoEventExceptionHandler());

        RequestDtoEventProducer requestDtoEventProducer = new RequestDtoEventProducer(disruptor.getRingBuffer());

        BeanRegisterUtils.registerSingleton(applicationContext,
                "RequestDtoEventDisruptorLifeCycleContainer",
                new DisruptorLifeCycleContainer("RequestDtoEventDisruptor", disruptor, StartupOrderConstants.DISRUPTOR_REQUEST_DTO));

        logger.info("Register Bean:[{}]", "RequestDtoEventDisruptorLifeCycleContainer");

        return requestDtoEventProducer;
    }


    @ConfigurationProperties(prefix = "request-disruptor")
    public static class DisruptorProperties {

        private int jvmQueueSize;

        public int getJvmQueueSize() {
            return jvmQueueSize;
        }

        public void setJvmQueueSize(int jvmQueueSize) {
            this.jvmQueueSize = jvmQueueSize;
        }
    }

    private RequestDtoEventBusinessHandler requestDtoEventBusinessHandler() {

        RequestDtoEventBusinessHandler requestDtoEventBusinessHandler = new RequestDtoEventBusinessHandler();
        requestDtoEventBusinessHandler.setItemRepository(applicationContext.getBean(ItemRepository.class));
        return requestDtoEventBusinessHandler;
    }

}
