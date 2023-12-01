package com.leduongw01.mlisserver.controller;

import com.leduongw01.mlisserver.model.MlisUser;
import com.leduongw01.mlisserver.service.MlisUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/mlis")
public class MlisUserController {
    @Autowired
    MlisUserService mlisUserService;

    @GetMapping("/login")
    MlisUser login(@RequestParam("username") String username,@RequestParam("password") String password){
        return mlisUserService.login(username, password);
    }

    @GetMapping("/loginwithtoken")
    MlisUser loginwithtoken(@RequestParam("username") String username,@RequestParam("token") String token){
        return mlisUserService.loginwithtoken(username, token);
    }

    @PostMapping("/register")
    String register(@RequestBody MlisUser mlisUser){
        log.info("registing");
        mlisUser.set_id(null);
        if (mlisUserService.register(mlisUser)){
            return "1";
        }
        return "0";
    }
}
