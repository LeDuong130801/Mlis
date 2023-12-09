package com.leduongw01.mlisserver.controller;

import com.leduongw01.mlisserver.model.StringValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testapi")
@Slf4j
public class TestApiController {
    @GetMapping("/getkey")
    public StringValue getKey(){
        log.info("get");
        return new StringValue("a", "MlisServer", "haha");
    }
}
