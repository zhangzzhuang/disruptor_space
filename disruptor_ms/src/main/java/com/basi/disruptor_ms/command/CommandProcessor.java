package com.basi.disruptor_ms.command;

import com.basi.disruptor_ms.entity.Command;

public interface CommandProcessor <T extends Command>{

    void process(T command);

    Class<T> getMatchClass();

}
