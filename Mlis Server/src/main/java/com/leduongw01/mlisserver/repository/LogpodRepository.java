package com.leduongw01.mlisserver.repository;

import com.leduongw01.mlisserver.model.Logpod;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LogpodRepository extends MongoRepository<Logpod, String> {
    public List<Logpod> getAllByType(String type);
    public List<Logpod> getAllByObjectId(String objId);
    public Logpod getLogpodBy_id(String id);
    public boolean existsLogpodBy_id(String id);
}
