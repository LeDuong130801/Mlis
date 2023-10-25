package com.leduongw01.mlisserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PodcastList {
    @Id
    String _id;
    String name;
    String createOn;
    String createBy;
    ArrayList<String> podListId;
    String userId;
    String status;
}
