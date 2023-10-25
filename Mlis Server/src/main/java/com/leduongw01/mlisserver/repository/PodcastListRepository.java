package com.leduongw01.mlisserver.repository;

import com.leduongw01.mlisserver.model.PodcastList;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

public interface PodcastListRepository extends MongoRepository<PodcastList, String> {
    PodcastList getPodcastListBy_id(String _id);
    boolean existsPodcastListBy_id(String _id);
    ArrayList<PodcastList> getAllByUserId(String userId);
}
