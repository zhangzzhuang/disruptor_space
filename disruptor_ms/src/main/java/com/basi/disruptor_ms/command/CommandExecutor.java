package com.basi.disruptor_ms.command;

/**
 * @param <T> 执行器
 */
public interface CommandExecutor<T extends CommandBuffer> {

    void execute(T commandBuffer);
}
