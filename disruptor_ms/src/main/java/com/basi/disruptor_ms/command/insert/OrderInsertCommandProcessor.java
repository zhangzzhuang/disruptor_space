package com.basi.disruptor_ms.command.insert;

import com.basi.disruptor_ms.command.CommandEventProducer;
import com.basi.disruptor_ms.command.CommandProcessor;

public class OrderInsertCommandProcessor implements CommandProcessor<OrderInsertCommand> {

    private final CommandEventProducer<OrderInsertCommand>[] commandEventProducers;

    private final int producerCount;

    public OrderInsertCommandProcessor(CommandEventProducer<OrderInsertCommand>[] commandEventProducers) {
        this.commandEventProducers = commandEventProducers;
        this.producerCount = commandEventProducers.length;
    }

    @Override
    public void process(OrderInsertCommand command) {

        // 根据商品ID去模
        int index = (int) (command.getItemId() % (long) this.producerCount);
        commandEventProducers[index].onData(command);
    }

    @Override
    public Class<OrderInsertCommand> getMatchClass() {
        return OrderInsertCommand.class;
    }
}
