package com.xiaoma.obsidiantonotion.threads;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NotionThreadFactory implements ThreadFactory {
    private static final String threadName="NotionThread-";
    private AtomicInteger atomicInteger=new AtomicInteger(0);
    @Override
    public Thread newThread(Runnable r) {
        Thread thread=new Thread(r,threadName+atomicInteger.getAndIncrement());
        thread.setDaemon(true);
        thread.setPriority(Thread.NORM_PRIORITY);

        return thread;
    }
}
