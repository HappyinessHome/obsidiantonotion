package com.xiaoma.obsidiantonotion.notion;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaoma.obsidiantonotion.interndatas.ApplicationProperties;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
@Component
public class NotionUploader {
    private String notionToken;
    private String uploadUrl;
    public NotionUploader(){
        this.notionToken=ApplicationProperties.notionToken;
        this.uploadUrl=ApplicationProperties.notionUploadUrl;
    }
    // 上传图片并获取图片 URL
    public  String uploadImage(File imageFile) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(uploadUrl);

        // 设置 Headers
        post.setHeader("Authorization", "Bearer " + notionToken);
       // post.setHeader("Notion-Version", "2021-05-13");

        // 设置上传内容
        FileBody fileBody = new FileBody(imageFile);

        HttpEntity entity = MultipartEntityBuilder.create()
                .addPart("file", fileBody)
                .build();

        post.setEntity(entity);

        // 执行请求
        String response = EntityUtils.toString(httpClient.execute(post).getEntity());
        httpClient.close();

        // 解析响应，获取图片 URL
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonResponse = mapper.readTree(response);
        return jsonResponse.get("file").get("url").asText();
    }
}
