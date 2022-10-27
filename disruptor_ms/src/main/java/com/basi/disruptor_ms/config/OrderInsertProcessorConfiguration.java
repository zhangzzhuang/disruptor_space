package com.basi.disruptor_ms.config;

import com.basi.disruptor_ms.command.*;
import com.basi.disruptor_ms.entity.CommandEvent;
import com.basi.disruptor_ms.entity.StartupOrderConstants;
import com.basi.disruptor_ms.handler.CommandEventDBHandler;
import com.basi.disruptor_ms.handler.CommandEventExceptionHandler;
import com.basi.disruptor_ms.handler.CommandEventGCHandler;
import com.basi.disruptor_ms.command.insert.OrderInsertCommand;
import com.basi.disruptor_ms.command.insert.OrderInsertCommandBuffer;
import com.basi.disruptor_ms.command.insert.OrderInsertCommandExecutor;
import com.basi.disruptor_ms.command.insert.OrderInsertCommandProcessor;
import com.basi.disruptor_ms.lifecycle.DisruptorLifeCycleContainer;
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
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.concurrent.Executors;

@Configuration
@EnableConfigurationProperties(OrderInsertProcessorConfiguration.Conf.class)
public class OrderInsertProcessorConfiguration implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(OrderInsertProcessorConfiguration.class);


    private ApplicationContext applicationContext;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CommandDispatcher commandDispatcher;

    @Autowired
    private Conf conf;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        this.applicationContext = applicationContext;
    }

    @Bean
    public OrderInsertCommandProcessor courseTakeInsertCmdProcessor() {

        logger.info("Configure OrderInsertCommandProcessor......");
        CommandEventProducer<OrderInsertCommand>[] commandEventProducerList = new CommandEventProducer[conf.getNum()];

        for (int i = 0; i < conf.getNum(); i++) {

            OrderInsertCommandBuffer cmdBuffer = new OrderInsertCommandBuffer(conf.getSqlBufferSize());
            OrderInsertCommandExecutor cmdExecutor = new OrderInsertCommandExecutor(jdbcTemplate);

            Disruptor<CommandEvent<OrderInsertCommand>> disruptor =
                    new Disruptor<>(new CommandEventFactory<>(), conf.getQueueSize(), Executors.defaultThreadFactory());

            disruptor.handleEventsWith(new CommandEventDBHandler(cmdBuffer, cmdExecutor)).then(new CommandEventGCHandler());

            disruptor.setDefaultExceptionHandler(new CommandEventExceptionHandler<>());

            commandEventProducerList[i] = new CommandEventProducer<>(disruptor.getRingBuffer());

            BeanRegisterUtils.registerSingleton(applicationContext, "CommandEvent<OrderInsertCommand>_DisruptorLifeCycleContainer_" + i,
                    new DisruptorLifeCycleContainer("CommandEvent<OrderInsertCommand>_Disruptor_" + i, disruptor, StartupOrderConstants.DISRUPTOR_ORDER_INSERT));
            logger.info("Register Bean:[{}]", "CommandEvent<OrderInsertCommand>_DisruptorLifeCycleContainer_" + i);
        }

        OrderInsertCommandProcessor cmdProcessor = new OrderInsertCommandProcessor(commandEventProducerList);

        commandDispatcher.registerCommandProcessor(cmdProcessor);
        return cmdProcessor;
    }


    @ConfigurationProperties(prefix = "order-insert.proc")
    public static class Conf {

        /**
         * 处理器数量
         */
        private int num;

        /**
         * 单次执行的SQL条数 (将多条SQL放到一起执行比分多次执行效率高)
         */
        private int sqlBufferSize;

        /**
         * disruptor队列长度, 值必须是2的次方
         */
        private int queueSize;

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getSqlBufferSize() {
            return sqlBufferSize;
        }

        public void setSqlBufferSize(int sqlBufferSize) {
            this.sqlBufferSize = sqlBufferSize;
        }

        public int getQueueSize() {
            return queueSize;
        }

        public void setQueueSize(int queueSize) {
            this.queueSize = queueSize;
        }

    }
}
