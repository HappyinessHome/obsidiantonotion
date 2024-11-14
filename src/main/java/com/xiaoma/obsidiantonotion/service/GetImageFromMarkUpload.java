package com.xiaoma.obsidiantonotion.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetImageFromMarkUpload {
    public void getImage(File file){
        // 读取文件内容
        String content = null;
        try {
            content = new String(Files.readAllBytes(file.toPath()));
            // 使用正则表达式匹配所有图片路径
            Pattern pattern = Pattern.compile("!\\[.*?\\]\\((.*?)\\)");
            Matcher matcher = pattern.matcher(content);

            // 使用正则表达式匹配并替换图片路径
            String updatedContent = content.replaceAll(Pattern.quote(oldPath), Matcher.quoteReplacement(newUrl));
            // 写回文件
            Files.write(file.toPath(), updatedContent.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
