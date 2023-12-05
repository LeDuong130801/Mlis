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
    private String _id;
    private String name;
    private String createOn;
    private List<String> podListId;
    private String userId;
    private String status;
    public Favorite(String id, List<String> podListId){
        _id = id;
        name = "Danh sách yêu thích";
        Date date = new Date();
        createOn = date.getTime()+"";
        this.podListId = podListId;
        userId = id;
        status = "1";
    }
    public Favorite(String id){
        _id = id;
        name = "Danh sách yêu thích";
        Date date = new Date();
        createOn = date.getTime()+"";
        this.podListId = new ArrayList<>();
        userId = id;
        status = "1";
    }
}
