package com.basi.disruptor_ms.entity;

public abstract class StartupOrderConstants {

    public static final int DISRUPTOR_REQUEST_DTO = 1;
    public static final int DISRUPTOR_ORDER_INSERT = 2;
    public static final int DISRUPTOR_ITEM_UPDATE = 3;
    //client先启动，server在启动
    public static final int MQ_CLIENT_START = 4;
    public static final int MQ_SERVER_START = 5;

    private StartupOrderConstants() {

    }
}
