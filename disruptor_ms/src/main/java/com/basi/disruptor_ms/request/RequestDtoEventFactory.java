package com.basi.disruptor_ms.request;

import com.lmax.disruptor.EventFactory;

public class RequestDtoEventFactory implements EventFactory<RequestDtoEvent> {

    @Override
    public RequestDtoEvent newInstance() {
        return new RequestDtoEvent();
    }
}
