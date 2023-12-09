package com.leduongw01.mlisserver.repository;

import com.leduongw01.mlisserver.model.MlisUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.List;

public interface MlisUserRepository extends MongoRepository<MlisUser, String> {
    boolean existsByUsername(String s);
    boolean existsMlisUserBy_id(String id);
    MlisUser getMlisUserBy_id(String _id);
    MlisUser getMlisUserByUsername(String username);
    boolean existsMlisUserByUsernameAndPassword(String username, String password);
    MlisUser getMlisUserByUsernameAndPassword(String username, String password);
    MlisUser getMlisUserByUsernameAndToken(String username, String token);
    boolean existsMlisUserByUsernameAndToken(String username, String token);
    boolean getMlisUserByGoogleAuth(String googleAuth);
    List<MlisUser> getAllBy_idIn(List<String> _id);
}
