package com.basi.disruptor_ms.handler;

import com.basi.disruptor_ms.entity.Command;
import com.basi.disruptor_ms.entity.CommandEvent;
import com.lmax.disruptor.ExceptionHandler;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandEventExceptionHandler<T extends Command> implements ExceptionHandler<CommandEvent<T>> {

    private static final Logger logger = LoggerFactory.getLogger(CommandEventExceptionHandler.class);

    @Override
    public void handleEventException(Throwable ex, long sequence, CommandEvent<T> event) {

        logger.error("{} : {}. {} ", event.getCommand().getClass().getName(), event.getCommand().getId(), ExceptionUtils.getStackTrace(ex));
    }

    @Override
    public void handleOnStartException(Throwable ex) {

        logger.error("Exception during onStart() : {}", ex);
    }

    @Override
    public void handleOnShutdownException(Throwable ex) {

        logger.error("Exception during onShutdown() : {}", ex);
    }
}
