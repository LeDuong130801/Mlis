package com.leduongw01.mlisserver.repository;

import com.leduongw01.mlisserver.model.MediaStoraged;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MediaStoragedRepository extends MongoRepository<MediaStoraged, String> {
    String getMediaStoragedBy_id(String id);
}
