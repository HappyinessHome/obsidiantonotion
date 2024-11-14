package com.xiaoma.obsidiantonotion.threads;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NotionThreadPools {
    public static ThreadPoolExecutor notionThreadPool(int corePoolSize, int maximumPoolSize){
        return new ThreadPoolExecutor(corePoolSize,maximumPoolSize,3,TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(),new NotionThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
    }
}
