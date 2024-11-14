package com.xiaoma.obsidiantonotion.service;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class GetImageFromMarkUpload {
    public void getImage(File file){
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

            }


            // 写回文件
           // Files.write(file.toPath(), updatedContent.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
