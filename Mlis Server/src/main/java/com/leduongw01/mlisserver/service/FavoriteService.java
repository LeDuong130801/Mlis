package com.leduongw01.mlisserver.service;

import com.leduongw01.mlisserver.model.Favorite;
import com.leduongw01.mlisserver.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {
    @Autowired
    FavoriteRepository favoriteRepository;
    public boolean changeFavoriteName(String favoriteId, String newName){
        if(favoriteRepository.existsPodcastListBy_id(favoriteId)){
            Favorite tmp = favoriteRepository.getFavoriteBy_id(favoriteId);
            tmp.setName(newName);
            favoriteRepository.save(tmp);
            return  true;
        }
        return false;
    }
    public boolean updateFavorite(Favorite favorite, String favoriteId){
        if(favoriteRepository.existsPodcastListBy_id(favoriteId)){
            favorite.set_id(favoriteId);
            favoriteRepository.save(favorite);
            return true;
        }
        return false;
    }
    public boolean deleteFavorite(String favoriteId){
        if(favoriteRepository.existsPodcastListBy_id(favoriteId)){
            Favorite favorite = favoriteRepository.getFavoriteBy_id(favoriteId);
            favorite.setStatus("-1");
            favoriteRepository.save(favorite);
            return true;
        }
        return false;
    }
    public Favorite createFavorite(Favorite favorite){
        return favoriteRepository.insert(favorite);
    }
    public Favorite getFavorite(String id){
        return favoriteRepository.getFavoriteBy_id(id);
    }
    public List<Favorite> getAllFavoriteByUserId(String userId){
        return favoriteRepository.getAllByUserIdAndStatus(userId, "1");
    }
    public List<Favorite> getAllFavoriteByStatus(String status){
        return favoriteRepository.getAllByStatus(status);
    }
}
