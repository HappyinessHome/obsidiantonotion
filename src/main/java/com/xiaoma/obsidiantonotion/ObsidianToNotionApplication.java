package com.xiaoma.obsidiantonotion;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public abstract class ObsidianToNotionApplication implements CommandLineRunner{

    public static void main(String[] args) {
        SpringApplication.run(ObsidianToNotionApplication.class, args)
        ;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("obsidianTonotion插件启动中...");

    }
}
