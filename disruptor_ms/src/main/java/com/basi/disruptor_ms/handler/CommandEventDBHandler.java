package com.basi.disruptor_ms.handler;

import com.basi.disruptor_ms.entity.Command;
import com.basi.disruptor_ms.command.CommandBuffer;
import com.basi.disruptor_ms.entity.CommandEvent;
import com.basi.disruptor_ms.command.CommandExecutor;
import com.lmax.disruptor.EventHandler;

public class CommandEventDBHandler<T extends Command> implements EventHandler<CommandEvent<T>> {

    private final CommandBuffer<T> commandBuffer;

    private final CommandExecutor commandExecutor;

    public CommandEventDBHandler(CommandBuffer<T> commandBuffer, CommandExecutor commandExecutor) {
        this.commandBuffer = commandBuffer;
        this.commandExecutor = commandExecutor;
    }

    @Override
    public void onEvent(CommandEvent<T> event, long l, boolean endOfBatch) throws Exception {

        if (!commandBuffer.hasRemaining()){
            flushBuffer();
        }

        commandBuffer.put(event.getCommand());

        if (endOfBatch){
            flushBuffer();
        }
    }

    private void flushBuffer(){

        commandExecutor.execute(commandBuffer);
        commandBuffer.clear();
    }
}
