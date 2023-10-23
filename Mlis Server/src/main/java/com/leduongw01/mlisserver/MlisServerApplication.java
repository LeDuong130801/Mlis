package com.leduongw01.mlisserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class MlisServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MlisServerApplication.class, args);
        log.info("Server đang chạy");
    }

}
