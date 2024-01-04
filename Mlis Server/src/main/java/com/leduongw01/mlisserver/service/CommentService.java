package com.leduongw01.mlisserver.service;

import com.leduongw01.mlisserver.model.Comment;
import com.leduongw01.mlisserver.model.ViewComment;
import com.leduongw01.mlisserver.repository.CommentRepository;
import com.leduongw01.mlisserver.repository.MlisUserRepository;
import com.leduongw01.mlisserver.repository.PodcastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    MlisUserRepository mlisUserRepository;
    @Autowired
    PodcastRepository podcastRepository;
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
    public List<Comment> getAllCommentByPodcastIdAndStatus(String podcastId, String status){
        return commentRepository.getAllByPodcastIdAndStatus(podcastId, status);
    }
    public void deleteComment(String commentId){
        if (commentRepository.existsCommentBy_id(commentId)){
            Comment comment = getCommentById(commentId);
            comment.setStatus("-1");
            commentRepository.save(comment);
        }
    }
    public List<ViewComment> getViewCommentByPodcastId(String podcastId){
        List<Comment> commentList = getAllCommentByPodcastId(podcastId);
        return getViewComments(commentList);
    }
    public List<ViewComment> getViewCommentByPodcastIdAndStatus(String podcastId, String status){
        List<Comment> commentList = getAllCommentByPodcastIdAndStatus(podcastId, status);
        return getViewComments(commentList);
    }
    public List<ViewComment> getAllViewComment(){
        List<Comment> commentList = commentRepository.getAllBy_idIsNotNullOrderByStatusDesc();
        return getViewComments(commentList);
    }

    private List<ViewComment> getViewComments(List<Comment> commentList) {
        List<ViewComment> viewComments = new ArrayList<>();
        for (Comment comment : commentList){
            String username = "Người dùng không xác định";
            if (mlisUserRepository.existsMlisUserBy_id(comment.getUserId())){
                username = mlisUserRepository.getMlisUserBy_id(comment.getUserId()).getUsername();
            }
            String podcastName = "Truyện đã bị xóa";
            if (podcastRepository.existsPodcastBy_id(comment.getPodcastId())){
                podcastName = podcastRepository.getPodcastBy_id(comment.getPodcastId()).getName();
            }
            ViewComment viewComment = new ViewComment();
            viewComment._id = comment.get_id();
            viewComment.cmtOn = comment.getCmtOn();
            viewComment.content = comment.getContent();
            viewComment.userId = comment.getUserId();
            viewComment.status = comment.getStatus();
            viewComment.username = username;
            viewComment.podcastName = podcastName;
            viewComments.add(viewComment);
        }
        return viewComments;
    }
    public String countComment(){
        return commentRepository.countCommentByStatus("1")+":"+ commentRepository.countCommentByStatusNot("1");
    }
}
