package com.xiaoma.obsidiantonotion.service;

import com.xiaoma.obsidiantonotion.notion.NotionUploader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class GetImageFromMarkUpload {
    private NotionUploader notionUploader;
    @Autowired
    public GetImageFromMarkUpload(NotionUploader notionUploader){
        this.notionUploader=notionUploader;
    }
    public void getImage(File file, Map<String,File> images){
        // 读取文件内容
        String content = null;
        try {
            content = new String(Files.readAllBytes(file.toPath()));
            // 使用正则表达式匹配所有图片路径
            Pattern pattern = Pattern.compile("!\\[\\[(.*?)\\]\\]\n");
            Matcher matcher = pattern.matcher(content);
            String updatedContent = content;
            while (matcher.find()) {
                String oldImagePath = matcher.group(1);  // 获取图片的原路径
                log.info("imagePath:{}",oldImagePath);
                File fileImage = getFileImage(oldImagePath, images);
                if(fileImage==null){
                    continue;
                }
                String imageUrl = notionUploader.uploadImage(fileImage);
                // 将图片路径替换为新的图片URL
                updatedContent = updatedContent.replaceAll(Pattern.quote(oldImagePath), Matcher.quoteReplacement(imageUrl));
            }


            // 写回文件
            Files.write(file.toPath(), updatedContent.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private File getFileImage(String imageName, Map<String,File> images){
        File file = images.get(imageName);
        return file;
    }
}
