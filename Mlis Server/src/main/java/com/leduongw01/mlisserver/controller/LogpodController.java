package com.leduongw01.mlisserver.controller;

import com.leduongw01.mlisserver.model.Logpod;
import com.leduongw01.mlisserver.model.StringValue;
import com.leduongw01.mlisserver.service.LogpodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/logpod")
public class LogpodController {
    @Autowired
    LogpodService logpodService;
    @PostMapping(value = "/send")
    public Logpod sendLog(@RequestBody Logpod logpod){
        return logpodService.sendLogpod(logpod);
    }

    @PostMapping("/delete")
    public boolean deleteById(@RequestParam("contentId") String id){
        return logpodService.deleteLogpodById(id);
    }

    @PostMapping("/getbytype")
    public List<Logpod> getAllByType(@RequestParam("contentType") String type){
        return logpodService.getAllByType(type);
    }

    @PostMapping("/getbyobjectid")
    public List<Logpod> getByObjectId(@RequestParam("contentObjectId") String objectId){
        return logpodService.getAllByObjectId(objectId);
    }

    @PostMapping("/getbyid")
    public Logpod getById(@RequestParam("contentId") String id){
        return logpodService.getLogpodById(id);
    }
}
