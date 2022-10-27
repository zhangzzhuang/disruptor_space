package com.basi.disruptor_ms.dataloader;

import org.springframework.context.SmartLifecycle;

/**
 * 启动时的数据加载器
 */
public abstract class DataStartupLoader implements SmartLifecycle {

    private volatile boolean running = false;

    private final Object monitor = new Object();

    @Override
    public boolean isAutoStartup() {
        return SmartLifecycle.super.isAutoStartup();
    }

    @Override
    public void start() {

        synchronized (this.monitor){
            if (!this.running){
                doLoad();
            }
            this.running = true;
        }
    }

    @Override
    public void stop(Runnable callback) {
        this.stop();
        callback.run();
    }

    @Override
    public void stop() {
        this.running = false;
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    public abstract void doLoad();
}
