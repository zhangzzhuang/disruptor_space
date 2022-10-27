package com.basi.disruptor_ms.request;

import com.basi.disruptor_ms.entity.RequestDto;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

public class RequestDtoEventProducer {

    private static final EventTranslatorOneArg<RequestDtoEvent, RequestDto> TRANSLATOR = ((requestDtoEvent, l, requestDto) -> requestDtoEvent.setRequestDto(requestDto));

    private final RingBuffer<RequestDtoEvent> ringBuffer;

    public RequestDtoEventProducer(RingBuffer<RequestDtoEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(RequestDto requestDto){
        ringBuffer.publishEvent(TRANSLATOR,requestDto);
    }
}
