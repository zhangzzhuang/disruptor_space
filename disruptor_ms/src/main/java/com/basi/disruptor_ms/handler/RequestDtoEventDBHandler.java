package com.basi.disruptor_ms.handler;

import com.basi.disruptor_ms.entity.Command;
import com.basi.disruptor_ms.command.CommandDispatcher;
import com.basi.disruptor_ms.request.RequestDtoEvent;
import com.lmax.disruptor.EventHandler;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class RequestDtoEventDBHandler implements EventHandler<RequestDtoEvent> {

    private CommandDispatcher commandDispatcher;

    public void setCommandDispatcher(CommandDispatcher commandDispatcher) {

        this.commandDispatcher = commandDispatcher;
    }

    @Override
    public void onEvent(RequestDtoEvent requestDtoEvent, long l, boolean b) throws Exception {

        if (requestDtoEvent.hasErrorOrException()) {
            return;
        }

        List<Command> commandList = requestDtoEvent.getCommandCollector().getCommandList();
        if (CollectionUtils.isEmpty(commandList)) {
            return;
        }

        for (Command command : commandList) {

            commandDispatcher.dispatch(command);
        }
    }
}
