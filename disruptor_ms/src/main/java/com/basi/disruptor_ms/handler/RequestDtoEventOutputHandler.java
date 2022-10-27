package com.basi.disruptor_ms.handler;

import com.basi.disruptor_ms.request.RequestDtoEvent;
import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestDtoEventOutputHandler implements EventHandler<RequestDtoEvent> {

    private static final Logger logger = LoggerFactory.getLogger(RequestDtoEventOutputHandler.class);

    @Override
    public void onEvent(RequestDtoEvent requestDtoEvent, long l, boolean b) throws Exception {

        // TODO: 2022/9/19 数据输出回请求方
    }
}
