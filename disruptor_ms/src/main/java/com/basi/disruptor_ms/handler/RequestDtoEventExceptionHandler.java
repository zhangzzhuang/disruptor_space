package com.basi.disruptor_ms.handler;

import com.basi.disruptor_ms.entity.ResponseDto;
import com.basi.disruptor_ms.request.RequestDtoEvent;
import com.lmax.disruptor.ExceptionHandler;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestDtoEventExceptionHandler implements ExceptionHandler<RequestDtoEvent> {

    private static final Logger logger = LoggerFactory.getLogger(RequestDtoEventExceptionHandler.class);


    @Override
    public void handleEventException(Throwable throwable, long l, RequestDtoEvent requestDtoEvent) {

        requestDtoEvent.setResponseDto(createExceptionResponseDto(requestDtoEvent.getResponseDto().getRequestId(), ExceptionUtils.getStackTrace(throwable)));
        logger.error("{}:{}.{}", requestDtoEvent.getRequestDto().getClass().getName(), requestDtoEvent.getRequestDto().getId(), ExceptionUtils.getStackTrace(throwable));
    }

    @Override
    public void handleOnStartException(Throwable throwable) {
        logger.error("Exception during onStart() : {}", throwable);
    }

    @Override
    public void handleOnShutdownException(Throwable throwable) {
        logger.error("Exception during OnShutdown() : {}", throwable);
    }

    private ResponseDto createExceptionResponseDto(String requestId, String exception) {

        ResponseDto responseDto = new ResponseDto(requestId);
        responseDto.setSuccess(false);
        responseDto.setErrorMessage(exception);
        return responseDto;

    }
}
