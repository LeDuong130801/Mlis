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
    String _id;
    String name;
    String author;
    String category;
    String createOn;
    String updateOn;
    String detail;
    String urlImg;
    String status;
}
