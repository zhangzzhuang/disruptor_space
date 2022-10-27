package com.basi.disruptor_ms.entity;

public class CommandEvent<T extends Command>{

    private T command;

    public T getCommand() {
        return command;
    }

    public void setCommand(T command) {
        this.command = command;
    }

    public void clearForGC(){
        this.command = null;
    }
}
