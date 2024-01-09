package com.leduongw01.mlisserver.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leduongw01.mlisserver.model.Playlist;
import com.leduongw01.mlisserver.model.Podcast;
import com.leduongw01.mlisserver.service.MediaStoragedService;
import com.leduongw01.mlisserver.service.PlaylistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/api/playlist")
public class PlaylistController {
    @Autowired
    PlaylistService playlistService;
    @Autowired
    MediaStoragedService mediaStoragedService;

    @GetMapping("/getbyidandstatus")
    Playlist getPlaylistById(@RequestParam("contentId") String id, @RequestParam("contentStatus") String status){
        return playlistService.getPlaylistByIdAndStatus(id, status);
    }
    @GetMapping("/getbysearch")
    List<Playlist> getBySearch(@RequestParam("keyword")String keyword){
        log.info("search: "+ keyword);
        return playlistService.getAllByNameContainAndStatus(keyword, "1");
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
    Playlist updatePlayList(@RequestParam(name = "image", required = false) MultipartFile image,@RequestParam("playlist") String playlistStr) throws JsonProcessingException {
        Playlist playlist = new ObjectMapper().readValue(playlistStr, Playlist.class);
        if (image!=null){
            String imageName = mediaStoragedService.storeFileImage(image);
            playlist.setUrlImg("http:\\\\192.168.1.35:8080\\storage\\files\\"+imageName);
        }
        return playlistService.updatePlayist(playlist);
    }
    @PostMapping("/createPlaylist")
    Playlist createPlaylist(@RequestParam(name = "fileImage", required = false) MultipartFile file, @RequestParam("playlist") String playlistStr) throws JsonProcessingException {
        Playlist playlist = new ObjectMapper().readValue(playlistStr, Playlist.class);
        String imageName = mediaStoragedService.storeFileImage(file);
        playlist.setUrlImg("http:\\\\192.168.1.35:8080\\storage\\files\\"+imageName);
        return playlistService.createPlayist(playlist);
    }
    @DeleteMapping("/deletePlaylist")
    Playlist deletePlaylist(@RequestParam("playlistId") String playlistId){
        return playlistService.deletePlaylist(playlistId);
    }
    @GetMapping("/count")
    String countPlaylist(){
        return playlistService.countPlaylist();
    }
}
