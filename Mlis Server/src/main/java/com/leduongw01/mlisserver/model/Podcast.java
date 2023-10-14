package com.leduongw01.mlisserver.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Podcast {
    @Id
    private String _id;
    private String name;
    private String createOn;
    private String createBy;
    private String url;
    private String status;
    public Podcast(String _id){
        this._id = _id;
    }
}
