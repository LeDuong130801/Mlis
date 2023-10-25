package com.leduongw01.mlisserver.repository;

import com.leduongw01.mlisserver.model.Podcast;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

public interface PodcastRepository extends MongoRepository<Podcast, String> {
    public Podcast getPodcastBy_id(String id);
    public ArrayList<Podcast> getAllByCreateBy(String createBy);
    public boolean existsPodcastBy_id(String id);
    ArrayList<Podcast> getAllByAuthor(String author);
    ArrayList<Podcast> getAllByStatus(String s);
}
