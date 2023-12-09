package com.leduongw01.mlisserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Comment {
    @Id
    String _id;
    String podcastId;
    String userId;
    String content;
    String cmtOn;
    String status;
}
