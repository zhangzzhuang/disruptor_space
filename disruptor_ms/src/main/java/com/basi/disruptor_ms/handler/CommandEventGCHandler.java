package com.basi.disruptor_ms.handler;

import com.basi.disruptor_ms.entity.Command;
import com.basi.disruptor_ms.entity.CommandEvent;
import com.lmax.disruptor.EventHandler;

public class CommandEventGCHandler<T extends Command> implements EventHandler<CommandEvent<T>> {

    @Override
    public void onEvent(CommandEvent<T> event, long sequence, boolean endOfBatch) throws Exception {
        event.clearForGC();
    }
}
