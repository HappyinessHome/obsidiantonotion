package com.xiaoma.obsidiantonotion.utils;

import java.io.File;
import java.util.List;
import java.util.Map;

public class GetFiles {
    public static void getAllFiles(String rootPath,List<File> fs){
        System.out.println(rootPath);
        File file=new File(rootPath);
        File[] files = file.listFiles();
        for (File listFile : files) {
            if(listFile.isDirectory()){
                getAllFiles(listFile.getPath(),fs);
            }else{
                fs.add(listFile);
            }
        }
    }

    public static void getAllFiles(String rootPath, Map<String,File> images){
        File file=new File(rootPath);
        File[] files = file.listFiles();
        for (File listFile : files) {
            if(listFile.isDirectory()){
                getAllFiles(listFile.getPath(),images);
            }else{
                images.put(listFile.getName(),listFile);
            }
        }
    }
}
