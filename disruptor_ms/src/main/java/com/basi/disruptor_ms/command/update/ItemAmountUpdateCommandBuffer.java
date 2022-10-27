package com.basi.disruptor_ms.command.update;

import com.basi.disruptor_ms.command.CommandBuffer;
import com.basi.disruptor_ms.exceptions.CommandBufferOverflowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemAmountUpdateCommandBuffer implements CommandBuffer<ItemAmountUpdateCommand> {

    private static final Logger logger = LoggerFactory.getLogger(ItemAmountUpdateCommandBuffer.class);

    private final Map<Long, ItemAmountUpdateCommand> commandMap = new HashMap<>();

    private final int capacity;

    public ItemAmountUpdateCommandBuffer(int capacity) {
        this.capacity = capacity;
    }


    @Override
    public List<ItemAmountUpdateCommand> get() {
        return new ArrayList<>(commandMap.values());
    }

    @Override
    public void put(ItemAmountUpdateCommand command) {

        Long itemId = command.getItemId();
        if (!hasRemaining() && commandMap.get(itemId) == null) {
            throw new CommandBufferOverflowException();
        }

        ItemAmountUpdateCommand prev = this.commandMap.put(itemId, command);
        //prevValue 不为空说明itemId重复了
        if (prev != null) {
            logger.info("Optimized Update Command: {}", command);
        }
        logger.info("Update Command Put Buffer Map: {}", command);

    }

    @Override
    public void clear() {
        commandMap.clear();
    }

    @Override
    public boolean hasRemaining() {
        return commandMap.size() < this.capacity;
    }
}
