package com.basi.disruptor_ms.service;

import com.alibaba.fastjson.JSON;
import com.basi.disruptor_ms.entity.RequestDto;
import com.basi.disruptor_ms.mq.MessageSender;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class SpikeCommandService {

    private static final Logger logger = LoggerFactory.getLogger(SpikeCommandService.class);

    @Autowired
    @Qualifier("requestMessageSender")
    private MessageSender requestMessageSender;


    public String doRequest(RequestDto requestDto){

        logger.info("userId : [{}] spike itemId : [{}]",requestDto.getUserId(),requestDto.getItemId());

        try {
            requestMessageSender.sendMessage(JSON.toJSONString(requestDto),requestDto.getId());
        } catch (Exception e){
            logger.error("messageSender error:[{}]", ExceptionUtils.getStackTrace(e));
        }

        return requestDto.getId();
    }
}
