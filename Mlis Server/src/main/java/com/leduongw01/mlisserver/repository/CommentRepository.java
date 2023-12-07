package com.leduongw01.mlisserver.repository;

import com.leduongw01.mlisserver.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    boolean existsCommentBy_id(String _id);
    boolean existsCommentBy_idAndStatus(String _id, String status);
    List<Comment> getAllByPodcastIdAndStatus(String podcastId, String status);
    List<Comment> getAllByPodcastId(String podcastId);
    List<Comment> getAllByStatus(String status);
    List<Comment> getAllByMlisUserIdAndStatus(String mlisUserId, String status);
    Comment getCommentBy_id(String _id);
}
