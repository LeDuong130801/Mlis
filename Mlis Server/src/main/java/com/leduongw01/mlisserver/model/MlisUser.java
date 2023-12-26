package com.leduongw01.mlisserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Random;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MlisUser {
    private String _id;
    private String username;
    private String password;
    private String email;
    private String googleAuth;
    private String status;
    private String token;
    private String dateOfBirth;
    private String gender;
    public void genToken(){
        Random random = new Random();
        setToken(Math.abs(random.nextInt())+"active"+new Date().getTime());
    }
}
