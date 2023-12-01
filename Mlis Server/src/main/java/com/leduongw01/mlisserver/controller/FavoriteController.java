package com.leduongw01.mlisserver.controller;

import com.leduongw01.mlisserver.model.Favorite;
import com.leduongw01.mlisserver.model.MlisUser;
import com.leduongw01.mlisserver.model.Podcast;
import com.leduongw01.mlisserver.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorite")
public class FavoriteController {
    @Autowired
    FavoriteService favoriteService;

    @GetMapping("/curd")
    Favorite getFavorite(@RequestParam("id") String id){
        return favoriteService.getFavorite(id);
    }

    @PostMapping("/curd")
    Favorite createFavorite(@RequestBody Favorite favorite){
        return favoriteService.createFavorite(favorite);
    }

    @PutMapping("/curd")
    String updateFavorite(@RequestBody Favorite favorite, @RequestParam("id") String favoriteId){
        if (favoriteService.updateFavorite(favorite, favoriteId)){
            return "UpdateSuccess";
        }
        return "UpdateFailed";
    }
    @DeleteMapping("/curd")
    String deleteFavorite(@RequestParam("id") String id){
        if (favoriteService.deleteFavorite(id)){
            return "DeleteSuccess";
        }
        return "DeleteFailed";
    }
    @GetMapping("/getalls")
    List<Favorite> getAllFavoriteByStatus(@RequestParam("status") String status){
        return favoriteService.getAllFavoriteByStatus(status);
    }
    @GetMapping("/getallu")
    List<Favorite> getAllFavoriteByUserId(@RequestParam("user") String userId){
        return favoriteService.getAllFavoriteByUserId(userId);
    }
    @PutMapping("/rename")
    String renameFavorite(@RequestParam("id") String id, @RequestParam("newname") String newName){
        if (favoriteService.changeFavoriteName(id, newName)){
            return "renameSuccess";
        }
        return "renameFailed";
    }
    @PostMapping("/addpodcasttofavorite")
    String addPodcastToFavorite(String mlisUserId, String podcastId, Favorite favorite){
        favoriteService.addToFavorite(mlisUserId, podcastId, favorite.get_id());
        return "200";
    }
    @PostMapping("/addpodcasttomainfavorite")
    String addPodcastToMainFavorite(@RequestParam("mlisUserId")String mlisUserId,@RequestBody List<String> podcastListId){
        favoriteService.addToMainFavorite(mlisUserId, podcastListId);
        return "200";
    }
}
