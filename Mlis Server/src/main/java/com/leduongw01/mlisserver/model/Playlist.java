package com.leduongw01.mlisserver.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Playlist {
    private String _id;
    private String name;
    private String author;
    private String category;
    private String createOn;
    private String updateOn;
    private String detail;
    private String urlImg;
    private String status;
}
