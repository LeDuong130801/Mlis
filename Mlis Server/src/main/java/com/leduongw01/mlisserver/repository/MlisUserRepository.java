package com.leduongw01.mlisserver.repository;

import com.leduongw01.mlisserver.model.MlisUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MlisUserRepository extends MongoRepository<MlisUser, String> {
    boolean existsByUsername(String s);
    MlisUser getMlisUserByUsername(String username);
    boolean getMlisUserByGoogleAuth(String googleAuth);
}
