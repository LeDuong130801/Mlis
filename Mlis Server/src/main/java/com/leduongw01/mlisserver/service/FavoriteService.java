package com.leduongw01.mlisserver.service;

import com.leduongw01.mlisserver.model.Favorite;
import com.leduongw01.mlisserver.model.MlisUser;
import com.leduongw01.mlisserver.model.Podcast;
import com.leduongw01.mlisserver.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FavoriteService {
    @Autowired
    FavoriteRepository favoriteRepository;
    public boolean changeFavoriteName(String favoriteId, String newName){
        if(favoriteRepository.existsFavoriteBy_id(favoriteId)){
            Favorite tmp = favoriteRepository.getFavoriteBy_id(favoriteId);
            tmp.setName(newName);
            favoriteRepository.save(tmp);
            return  true;
        }
        return false;
    }
    public boolean updateFavorite(Favorite favorite, String favoriteId){
        if(favoriteRepository.existsFavoriteBy_id(favoriteId)){
            favorite.set_id(favoriteId);
            favoriteRepository.save(favorite);
            return true;
        }
        return false;
    }
    public boolean deleteFavorite(String favoriteId){
        if(favoriteRepository.existsFavoriteBy_id(favoriteId)){
            Favorite favorite = favoriteRepository.getFavoriteBy_id(favoriteId);
            favorite.setStatus("-1");
            favoriteRepository.save(favorite);
            return true;
        }
        return false;
    }
    public Favorite createFavorite(Favorite favorite){
        favorite.setCreateOn((new Date()).getTime()+"");
        return favoriteRepository.insert(favorite);
    }
    public Favorite addToMainFavorite(String mlisUserId, List<String> podcastListId){
        if (!favoriteRepository.existsFavoriteBy_id(mlisUserId)){
            Favorite favorite = new Favorite(mlisUserId, podcastListId);
            return favoriteRepository.insert(favorite);
        }
        else{
            Favorite favorite = favoriteRepository.getFavoriteBy_id(mlisUserId);
            for (String podcastId: podcastListId){
                if (!favorite.getPodListId().contains(podcastId)){
                    favorite.getPodListId().add(podcastId);
                }
            }
            return favoriteRepository.save(favorite);
        }
    }
    public Favorite removeToMainFavorite(String mlisUserId, List<String> podcastListId){
        if (!favoriteRepository.existsFavoriteBy_id(mlisUserId)){
            Favorite favorite = new Favorite(mlisUserId);
            return favoriteRepository.insert(favorite);
        }
        else{
            Favorite favorite = favoriteRepository.getFavoriteBy_id(mlisUserId);
            for (String podcastId: podcastListId){
                int index = favorite.getPodListId().indexOf(podcastId);
                if (index!=-1){
                    favorite.getPodListId().remove(index);
                }
            }
            return favoriteRepository.save(favorite);
        }
    }
    public Favorite addToFavorite(String mlisUserId, String podcastId, String favoriteId){
        if (favoriteRepository.existsFavoriteBy_idAndUserId(favoriteId, mlisUserId)){
            Favorite favorite = favoriteRepository.getFavoriteBy_id(favoriteId);
            if (!favorite.getPodListId().contains(podcastId)){
                favorite.getPodListId().add(podcastId);
            }
            favorite.getPodListId().add(podcastId);
            return favoriteRepository.save(favorite);
        }
        return null;
    }
    public Favorite getFavorite(String id){
        return favoriteRepository.getFavoriteBy_id(id);
    }
    public List<Favorite> getAllFavoriteByUserId(String userId){
        if (!favoriteRepository.existsFavoriteBy_id(userId)){
        Favorite favorite = new Favorite(userId);
        favoriteRepository.insert(favorite);
    }
        return favoriteRepository.getAllByUserIdAndStatus(userId, "1");
    }
    public List<Favorite> getAllFavoriteByStatus(String status){
        return favoriteRepository.getAllByStatus(status);
    }
}
