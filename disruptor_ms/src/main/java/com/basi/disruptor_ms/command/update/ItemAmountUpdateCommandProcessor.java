package com.basi.disruptor_ms.command.update;

import com.basi.disruptor_ms.command.CommandEventProducer;
import com.basi.disruptor_ms.command.CommandProcessor;

public class ItemAmountUpdateCommandProcessor implements CommandProcessor<ItemAmountUpdateCommand> {

    private final CommandEventProducer<ItemAmountUpdateCommand>[] commandEventProducers;

    private final int producerCount;

    public ItemAmountUpdateCommandProcessor(CommandEventProducer<ItemAmountUpdateCommand>[] commandEventProducers) {
        this.commandEventProducers = commandEventProducers;
        this.producerCount = commandEventProducers.length;
    }

    @Override
    public void process(ItemAmountUpdateCommand command) {
        // 根据商品ID去模
        int index = (int) (command.getItemId() % (long) this.producerCount);
        commandEventProducers[index].onData(command);
    }

    @Override
    public Class<ItemAmountUpdateCommand> getMatchClass() {

        return ItemAmountUpdateCommand.class;
    }
}
