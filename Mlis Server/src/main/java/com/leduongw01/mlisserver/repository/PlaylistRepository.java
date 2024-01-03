package com.leduongw01.mlisserver.repository;

import com.leduongw01.mlisserver.model.Playlist;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PlaylistRepository extends MongoRepository<Playlist, String> {
    List<Playlist> getAllByStatus(String status);
    Playlist getPlaylistBy_id(String _id);
    Playlist getPlaylistBy_idAndStatus(String _id, String status);
    boolean existsAllBy_idAndStatus(String _id, String status);
    List<Playlist> getAllByAuthor(String author);
    List<Playlist> getAllByAuthorAndStatus(String author, String status);
    List<Playlist> getAllBy();
    List<Playlist> getAllBy_idIsNotNullOrderByStatusDesc();
    List<Playlist> getAllByNameContainsOrAuthorContainsOrCategoryContainsAndStatus(String name, String author, String category, String status);
    int countPlaylistByStatus(String status);
}
