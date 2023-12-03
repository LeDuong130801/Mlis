package com.leduongw01.mlisserver.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Podcast {
    private String _id;
    private String name;
    private String detail;
    private String createOn;
    private String updateOn;
    private String url;
    private String urlImg;
    private String playlistId;
    private String status;
    public Podcast(String _id){
        this._id = _id;
    }
}
