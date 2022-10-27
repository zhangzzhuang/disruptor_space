package com.basi.disruptor_ms.handler;

import com.basi.disruptor_ms.command.update.ItemAmountUpdateCommand;
import com.basi.disruptor_ms.command.insert.OrderInsertCommand;
import com.basi.disruptor_ms.entity.RequestDto;
import com.basi.disruptor_ms.entity.ResponseDto;
import com.basi.disruptor_ms.memdb.Item;
import com.basi.disruptor_ms.memdb.ItemRepository;
import com.basi.disruptor_ms.request.CommandCollector;
import com.basi.disruptor_ms.request.RequestDtoEvent;
import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestDtoEventBusinessHandler implements EventHandler<RequestDtoEvent> {

    private static final Logger logger = LoggerFactory.getLogger(RequestDtoEventBusinessHandler.class);

    private ItemRepository itemRepository;

    public void setItemRepository(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public void onEvent(RequestDtoEvent event, long l, boolean b) {

        RequestDto requestDto = event.getRequestDto();
        Item item = itemRepository.get(requestDto.getItemId());

        ResponseDto responseDto = new ResponseDto(requestDto.getId());

        if (item == null) {
            responseDto.setSuccess(false);
            responseDto.setErrorMessage("内存中还未缓存商品数据");
        } else if (item.decreaseAmount()) {
            logger.info("当前库存:[{}]", item);
            responseDto.setSuccess(true);
            CommandCollector collector = event.getCommandCollector();
            collector.addCommand(new ItemAmountUpdateCommand(requestDto.getId(), item.getId(), item.getAmount()));
            collector.addCommand(new OrderInsertCommand(requestDto.getId(), item.getId(), requestDto.getUserId()));
        } else {
            responseDto.setSuccess(false);
            responseDto.setErrorMessage("库存不足");
        }

        event.setResponseDto(responseDto);
    }
}
