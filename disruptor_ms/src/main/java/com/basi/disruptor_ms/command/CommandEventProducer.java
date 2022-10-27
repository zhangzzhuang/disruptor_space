package com.basi.disruptor_ms.command;

import com.basi.disruptor_ms.entity.Command;
import com.basi.disruptor_ms.entity.CommandEvent;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

public class CommandEventProducer<T extends Command> {

    private final EventTranslatorOneArg<CommandEvent,T> TRANSLATOR = (event, sequence, command) -> event.setCommand(command);

    private final RingBuffer<CommandEvent<T>> ringBuffer;

    public CommandEventProducer(RingBuffer<CommandEvent<T>> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(T command){

        ringBuffer.publishEvent((EventTranslatorOneArg)TRANSLATOR,command);
    }
}
