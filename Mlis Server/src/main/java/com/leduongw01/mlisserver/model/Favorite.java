package com.leduongw01.mlisserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Favorite {
    @Id
    String _id;
    String name;
    String createOn;
    List<String> podListId;
    String userId;
    String status;
    public Favorite(String id, List<String> podListId){
        _id = id;
        name = "Danh sách yêu thích";
        Date date = new Date();
        createOn = date.getTime()+"";
        this.podListId = podListId;
        userId = id;
        status = "1";
    }
}
