package com.basi.disruptor_ms.handler;

import com.basi.disruptor_ms.request.RequestDtoEvent;
import com.lmax.disruptor.EventHandler;

public class RequestDtoEventGCHandler implements EventHandler<RequestDtoEvent> {

    @Override
    public void onEvent(RequestDtoEvent requestDtoEvent, long l, boolean b) throws Exception {

        requestDtoEvent.clearForGc();
    }
}
