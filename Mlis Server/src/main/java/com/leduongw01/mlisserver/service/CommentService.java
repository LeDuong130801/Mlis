package com.leduongw01.mlisserver.service;

import com.leduongw01.mlisserver.model.Comment;
import com.leduongw01.mlisserver.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;
    public Comment getCommentById(String id){
        return commentRepository.getCommentBy_id(id);
    }
    public Comment sendComment(Comment comment){
        comment.set_id(null);
        comment.setCmtOn(new Date().getTime()+"");
        return commentRepository.insert(comment);
    }
    public List<Comment> getAllCommentByPodcastId(String podcastId){
        return commentRepository.getAllByPodcastId(podcastId);
    }
    public List<Comment> getAllCOmmentByPodcastIdAndStatus(String podcastId, String status){
        return commentRepository.getAllByPodcastIdAndStatus(podcastId, status);
    }
    public void deleteComment(String commentId){
        if (commentRepository.existsCommentBy_id(commentId)){
            Comment comment = getCommentById(commentId);
            comment.setStatus("-1");
            commentRepository.save(comment);
        }
    }
}
