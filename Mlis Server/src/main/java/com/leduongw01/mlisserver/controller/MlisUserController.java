package com.leduongw01.mlisserver.controller;

import com.leduongw01.mlisserver.model.MlisUser;
import com.leduongw01.mlisserver.model.StringValue;
import com.leduongw01.mlisserver.model.ViewMlisUser;
import com.leduongw01.mlisserver.service.MlisUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin
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
    @PutMapping("/changepass")
    String changePass(@RequestBody StringValue stringValue){
        return mlisUserService.changePass(stringValue);
    }
    @GetMapping("/getname")
    String getName(@RequestParam("userId")String userId, @RequestParam("pos") int pos){
        return mlisUserService.getUsernameById(userId);
    }
    @GetMapping("/getlistname")
    List<StringValue> getListName(@RequestBody List<String> listId){
        return mlisUserService.getListNameByListId(listId);
    }
    @GetMapping("/getviewmlisuser")
    List<ViewMlisUser> getViewMlisUser(){
        return mlisUserService.getViewMlisUser();
    }
    @DeleteMapping("/deletemlisuser")
    boolean deleteMlisUser(@RequestParam("userId")String userId){
        return mlisUserService.deleteUser(userId);
    }
    @DeleteMapping("/deletemlisuserandcomment")
    boolean deleteMlisUserAndComment(@RequestParam("userId")String userId){
        return mlisUserService.deleteUserAndComment(userId);
    }
    @GetMapping("/count")
    String countMlisUser(){ return mlisUserService.countMlisUser(); }
}
