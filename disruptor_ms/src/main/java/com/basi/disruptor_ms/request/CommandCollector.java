package com.basi.disruptor_ms.request;

import com.basi.disruptor_ms.entity.Command;

import java.util.ArrayList;
import java.util.List;

public class CommandCollector {

    private List<Command> commandList = new ArrayList<>(4);

    public List<Command> getCommandList() {
        return commandList;
    }

    public void addCommand(Command command) {
        commandList.add(command);
    }

}
