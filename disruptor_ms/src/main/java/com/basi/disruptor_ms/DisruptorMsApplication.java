package com.basi.disruptor_ms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.basi.disruptor_ms.mapper")
public class DisruptorMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DisruptorMsApplication.class, args);
    }

}
