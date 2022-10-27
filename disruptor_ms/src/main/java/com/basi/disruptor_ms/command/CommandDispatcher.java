package com.basi.disruptor_ms.command;

import com.basi.disruptor_ms.entity.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class CommandDispatcher{

    private static final Logger logger = LoggerFactory.getLogger(CommandDispatcher.class);

    private final Map<Class,CommandProcessor> commandProcessorMap = new HashMap<>();

    public void dispatch(Command command) {

        commandProcessorMap.get(command.getClass()).process(command);
    }


    public void registerCommandProcessor(CommandProcessor commandProcessor) {

        logger.info("Register CommandDispatcher:[{}] for [{}]",commandProcessor.getClass().getName(),commandProcessor.getMatchClass().getName());
        commandProcessorMap.put(commandProcessor.getMatchClass(),commandProcessor);
    }
}
