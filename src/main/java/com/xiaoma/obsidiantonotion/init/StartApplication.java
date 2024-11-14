package com.xiaoma.obsidiantonotion.init;


import com.xiaoma.obsidiantonotion.interndatas.ApplicationProperties;
import com.xiaoma.obsidiantonotion.service.GetImageFromMarkUpload;
import com.xiaoma.obsidiantonotion.threads.NotionThreadPools;
import com.xiaoma.obsidiantonotion.utils.GetFiles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@Component
public class StartApplication implements ApplicationListener<ContextRefreshedEvent> {
    @Value("${notion.token}")
    private String notionToken;
    @Value("${notion.pageId}")
    private String notionPageId;
    @Value("${notion.uploadUrl}")
    private String notionUploadUrl;
    @Value("${note.rootfile}")
    private String noteRootFile;
    @Value("${images.rootfile}")
    private String imagesRootFile;
    private GetImageFromMarkUpload getImageFromMarkUpload;
    @Autowired
    public StartApplication(GetImageFromMarkUpload getImageFromMarkUpload){
        this.getImageFromMarkUpload=getImageFromMarkUpload;
    }
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationProperties.notionPageId=notionPageId;
        ApplicationProperties.notionToken=notionToken;
        ApplicationProperties.notionUploadUrl=notionUploadUrl;
        ApplicationProperties.noteRootFile=noteRootFile;
        ApplicationProperties.imagesRootFile=imagesRootFile;
        log.info("获取到配置的notionToken:{},notionPageId:{},notionUploadUrl:{},笔记根目录:{},图片根目录:{}",notionToken,notionPageId,notionUploadUrl,noteRootFile,imagesRootFile);
        log.info("开始根据当前笔记根目录扫描");
        List<File> markdownFiles=new ArrayList<>();
        GetFiles.getAllFiles(noteRootFile,markdownFiles);
        log.info("共获取到笔记:{}个",markdownFiles.size());
        log.info("开始根据当前图片根目录扫描");
        Map<String,File> images=new ConcurrentHashMap<>();
        GetFiles.getAllFiles(imagesRootFile,images);
        log.info("获取到图片:{}个",images.size());
        log.info(markdownFiles.toString(),images.toString());
        int cpus = Runtime.getRuntime().availableProcessors();
        log.info("获取到当前机器的核心数为:{},由于任务需要请求notionApi属于IO型,故核心线程池数量设置为{}:",cpus,cpus*2,cpus*3);

        ThreadPoolExecutor threadPoolExecutor = NotionThreadPools.notionThreadPool(cpus * 2, cpus * 3);
        List<Callable<Void>> tasks = new ArrayList<>();
        markdownFiles.forEach(file -> {
            tasks.add(() -> {
                getImageFromMarkUpload.getImage(file, images);
                return null;  // 这里返回 Void，因为我们不需要返回值
            });
        });
        try {
            List<Future<Void>> futures = threadPoolExecutor.invokeAll(tasks);
            futures.forEach(voidFuture -> {
                try {
                    voidFuture.get();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("上传修改完毕");
    }
}
