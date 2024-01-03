package com.leduongw01.mlisserver.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ViewPodcast {
    private String _id;
    private String name;
    private String detail;
    private String createOn;
    private String updateOn;
    private String url;
    private String urlImg;
    private String playlist;
    private String playlistId;
    private String author;
    private String category;
    private String status;
}
