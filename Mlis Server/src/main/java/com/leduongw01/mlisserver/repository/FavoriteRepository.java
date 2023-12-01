package com.leduongw01.mlisserver.repository;

import com.leduongw01.mlisserver.model.Favorite;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FavoriteRepository extends MongoRepository<Favorite, String> {
    Favorite getFavoriteBy_id(String _id);
    boolean existsFavoriteBy_id(String _id);
    boolean existsFavoriteBy_idAndUserId(String _id, String userId);
    List<Favorite> getAllByUserIdAndStatus(String userId, String status);
    List<Favorite> getAllByStatus(String status);
}
