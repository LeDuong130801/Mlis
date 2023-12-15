package com.leduongw01.mlisserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Calendar;
import java.util.Date;

@SpringBootApplication
@Slf4j
public class MlisServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MlisServerApplication.class, args);
        log.info("Server đang chạy");
//        Date date = new Date(2023, Calendar.DECEMBER, 14);
//        Date date1 = new Date(2023, Calendar.DECEMBER, 15);
//        log.info(String.valueOf(date1.getTime()-date.getTime()));
    }

}
