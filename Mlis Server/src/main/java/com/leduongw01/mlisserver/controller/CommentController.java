package com.leduongw01.mlisserver.controller;

import com.leduongw01.mlisserver.model.Comment;
import com.leduongw01.mlisserver.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/comment")
@RestController
public class CommentController {
    @Autowired
    CommentService commentService;

    @PostMapping("/send")
    Comment sendComent(@RequestParam("userId") String userId,@RequestBody Comment comment){
        if (!comment.getMlisUserId().equals(userId)) {
            comment.setMlisUserId(userId);
        }
        return commentService.sendComment(comment);
    }

    @GetMapping("/view")
    List<Comment> getAllCommentByPodcastId(@RequestParam("podcastId")String podcastId){
        return commentService.getAllCommentByPodcastId(podcastId);
    }
    @GetMapping("/viewComment")
    List<Comment> getAllCommentByPodcastIdAndStatus(@RequestParam("podcastId") String podcastId, @RequestParam(value = "status", defaultValue = "1")String status){
        return commentService.getAllCOmmentByPodcastIdAndStatus(podcastId, status);
    }
    @PutMapping("/deleteComment")
    void deleteComment(@RequestParam("commentId")String commentId){
        commentService.deleteComment(commentId);
    }

}
