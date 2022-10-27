package com.example.disruptor_crud.firstexperience;

import lombok.Data;

@Data
public class Order {

    //订单ID
    private long id;

    //订单信息
    private String info;

    //订单价格
    private double price;

}
