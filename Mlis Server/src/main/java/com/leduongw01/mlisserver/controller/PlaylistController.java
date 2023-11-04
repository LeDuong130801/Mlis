package com.leduongw01.mlisserver.controller;


import com.leduongw01.mlisserver.model.Playlist;
import com.leduongw01.mlisserver.service.PlaylistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/playist")
public class PlaylistController {
    @Autowired
    PlaylistService playlistService;

    @GetMapping("/getbyidandstatus")
    Playlist getPlaylistById(@RequestParam("contentId") String id, @RequestParam("contentStatus") String status){
        return playlistService.getPlaylistByIdAndStatus(id, status);
    }

    @GetMapping("/getbyidallowed")
    Playlist getPlayListById(@RequestParam("contentId") String id){
        return playlistService.getPlaylistAllowedById(id);
    }

    @GetMapping("/getallbyAuthorAndStatusAndPage")
    List<Playlist> getAllByAuthorStatusPage(@RequestParam("contentAuthor") String author,@RequestParam("contentStatus") String status, @RequestParam("contentPage") Integer page, @RequestParam("contentQuantity") Integer quantity){
        return playlistService.getAllPlayistByAuthorAndPage(author, status, page, quantity);
    }
    @GetMapping("/getallbyAuthorAndStatus")
    List<Playlist> getAllByAuthorStatus(@RequestParam("contentAuthor") String author,@RequestParam("contentStatus") String status){
        return playlistService.getAllPlayistByAuthorAndStatus(author, status);
    }

    @GetMapping("/getallbyStatus")
    List<Playlist> getAllByStatus(@RequestParam("contentStatus")String status){
        return playlistService.getAllPlayistByStatus(status);
    }
    @GetMapping("/getallbyStatusPage")
    List<Playlist> getAllByStatusAndPage(@RequestParam("contentStatus") String status, @RequestParam("contentPage")Integer page, @RequestParam("contentQuantity")Integer quantity){
        return playlistService.getAllPlayistByStatusAndPage(status, page, quantity);
    }

    @GetMapping("/getall")
    List<Playlist> getAllPlaylist(){
        return playlistService.getAllPlayist();
    }
    @GetMapping("/getbyIdAndStatus")
    Playlist getPlaylistByIdAndStatus(@RequestParam("contentId")String id, @RequestParam("contentStatus")String status){
        return playlistService.getPlaylistByIdAndStatus(id, status);
    }

    @PostMapping("/updatePlaylist")
    Playlist updatePlayList(@RequestBody Playlist playlist){
        return  playlistService.updatePlayist(playlist);
    }
    @PostMapping("/createPlaylist")
    Playlist createPlaylist(@RequestBody Playlist playlist){
        return playlistService.createPlayist(playlist);
    }
    @DeleteMapping("/deletePlaylist")
    Playlist deletePlaylist(@RequestBody Playlist playlist){
        return playlistService.deletePlaylist(playlist);
    }
}
