package com.leduongw01.mlisserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MlisUser {
    String _id;
    String username;
    String password;
    String email;
    String googleAuth;
    String status;
}
