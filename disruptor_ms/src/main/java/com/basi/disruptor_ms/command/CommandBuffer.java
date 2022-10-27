package com.basi.disruptor_ms.command;

import com.basi.disruptor_ms.entity.Command;

import java.util.List;

public interface CommandBuffer<T extends Command>{

    List<T> get();


    void put(T command);

    /**
     * 清空缓存
     */
    void clear();

    /**
     * Buffer是否已经满了
     *
     * @return
     */
    boolean hasRemaining();
}
