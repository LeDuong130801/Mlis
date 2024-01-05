package com.leduongw01.mlisserver.service;

import com.leduongw01.mlisserver.model.Playlist;
import com.leduongw01.mlisserver.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PlaylistService {
    @Autowired
    PlaylistRepository playlistRepository;
    public Playlist updatePlayist(Playlist playlist){
        if(playlistRepository.existsAllBy_idAndStatus(playlist.get_id(), "1")){
            Playlist p = playlistRepository.getPlaylistBy_id(playlist.get_id());
            playlist.setStatus("1");
            Date d = new Date();
            long time = d.getTime();
            playlist.setCreateOn(p.getCreateOn());
            if (playlist.getUrlImg().equals("0"))
                playlist.setUrlImg(p.getUrlImg());
            playlist.setUpdateOn(time+"");
            return playlistRepository.save(playlist);
        }
        return null;
    }
    public Playlist createPlayist(Playlist playlist){
        if(playlistRepository.existsAllBy_idAndStatus(playlist.get_id(), "1")){
            return null;
        }
        playlist.setStatus("1");
        Date d = new Date();
        long time = d.getTime();
        playlist.setUpdateOn(time+"");
        playlist.setCreateOn(time+"");
        return playlistRepository.save(playlist);
    }
    public Playlist deletePlaylist(String playlistId){
        if(playlistRepository.existsAllBy_idAndStatus(playlistId, "1")){
            Playlist playlist = playlistRepository.getPlaylistBy_id(playlistId);
            playlist.setStatus("-1");
            Date d = new Date();
            long time = d.getTime();
            playlist.setUpdateOn(time+"");
            return playlistRepository.save(playlist);
        }
        return null;
    }
    public Playlist getPlaylistByIdAndStatus(String id, String status){
        if (playlistRepository.existsAllBy_idAndStatus(id, status)){
            return playlistRepository.getPlaylistBy_idAndStatus(id, status);
        }
        else return null;
    }
    public Playlist getPlaylistAllowedById(String id){
        if (playlistRepository.existsAllBy_idAndStatus(id, "1")){
            return playlistRepository.getPlaylistBy_idAndStatus(id, "1");
        }
        else return null;
    }
    public List<Playlist> getAllByNameContainAndStatus(String keyword, String status){
        return playlistRepository.getAllByNameContainsOrAuthorContainsOrCategoryContainsAndStatus(keyword, keyword, keyword, status);
    }
    public List<Playlist> getAllPlayistByStatus(String status){
        return playlistRepository.getAllByStatus(status);
    }
    public List<Playlist> getAllPlayistByStatusAndPage(String status, Integer page, Integer quantity){
        List<Playlist> playlists = playlistRepository.getAllByStatus(status);
        return playlists.subList((page-1)*quantity, page*quantity);
    }
    public List<Playlist> getAllPlayistByAuthorAndStatus(String author, String status){
        return playlistRepository.getAllByAuthorAndStatus(author, status);
    }
    public List<Playlist> getAllPlayistByAuthorAndPage(String author,String status, Integer page, Integer quantity){
        List<Playlist> playlists = playlistRepository.getAllByAuthorAndStatus(author, status);
        return playlists.subList((page-1)*quantity, page*quantity);
    }
    public List<Playlist> getAllPlayist(){
        return playlistRepository.getAllBy_idIsNotNullOrderByStatusDesc();
    }
    public String countPlaylist(){
        return playlistRepository.countPlaylistByStatus("1")+":"+ playlistRepository.countPlaylistByStatusNot("1");
    }
}
