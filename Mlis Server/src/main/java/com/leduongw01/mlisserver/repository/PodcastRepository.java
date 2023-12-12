package com.leduongw01.mlisserver.repository;

import com.leduongw01.mlisserver.model.Podcast;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PodcastRepository extends MongoRepository<Podcast, String> {
    public Podcast getPodcastBy_id(String id);
    public boolean existsPodcastBy_id(String id);
//    List<Podcast> getAllByAuthor(String author);
    List<Podcast> getAllByStatus(String s);
    List<Podcast> getAllBy_idIsNotNull();
}
