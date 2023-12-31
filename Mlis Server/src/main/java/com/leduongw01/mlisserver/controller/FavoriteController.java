package com.leduongw01.mlisserver.controller;

import com.leduongw01.mlisserver.model.Favorite;
import com.leduongw01.mlisserver.model.MlisUser;
import com.leduongw01.mlisserver.model.Podcast;
import com.leduongw01.mlisserver.service.FavoriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
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
        log.info("delete"+ id);
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
    Favorite addPodcastToFavorite(@RequestParam("mlisUserId") String mlisUserId,@RequestParam("podcastId") String podcastId,@RequestBody Favorite favorite){
        return favoriteService.addToFavorite(mlisUserId, podcastId, favorite);
    }
    @PostMapping("/removepodcasttofavorite")
    Favorite removePodcastToFavorite(@RequestParam("mlisUserId") String mlisUserId,@RequestParam("podcastId") String podcastId,@RequestBody Favorite favorite){
        return favoriteService.removeToFavorite(mlisUserId, podcastId, favorite);
    }
    @PostMapping("/addpodcasttomainfavorite")
    Favorite addPodcastToMainFavorite(@RequestParam("mlisUserId")String mlisUserId,@RequestBody List<String> podcastListId){
        return favoriteService.addToMainFavorite(mlisUserId, podcastListId);
    }
    @PutMapping("/removepodcasttomainfavorite")
    Favorite removePodcastToMainFavorite(@RequestParam("mlisUserId")String mlisUserId,@RequestBody List<String> podcastListId){
        return favoriteService.removeToMainFavorite(mlisUserId, podcastListId);
    }
}
