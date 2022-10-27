package com.basi.disruptor_ms.command;

import com.basi.disruptor_ms.entity.Command;
import com.basi.disruptor_ms.entity.CommandEvent;
import com.lmax.disruptor.EventFactory;

public class CommandEventFactory<T extends Command> implements EventFactory<CommandEvent<T>> {

    @Override
    public CommandEvent<T> newInstance() {
        return new CommandEvent<>();
    }
}
