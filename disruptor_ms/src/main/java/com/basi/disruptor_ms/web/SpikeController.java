package com.basi.disruptor_ms.web;

import com.basi.disruptor_ms.entity.RequestDto;
import com.basi.disruptor_ms.service.SpikeCommandService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 秒杀Rest Controller
 */
@RestController
public class SpikeController {

    @Autowired
    private SpikeCommandService spikeCommandService;

    /**
     * 下单
     *
     * @param itemId
     * @return
     */
    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public RequestDto order(@RequestParam("itemId") Long itemId){

        RequestDto requestDto = new RequestDto(itemId,getUser());
        spikeCommandService.doRequest(requestDto);
        return requestDto;
    }



    /**
     * 假的，获得当前用户名的方法
     *
     * @return
     */
    private String getUser() {
        return RandomStringUtils.randomAlphabetic(5);
    }

}
