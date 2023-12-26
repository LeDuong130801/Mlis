package com.leduongw01.mlisserver.service;

import com.leduongw01.mlisserver.model.Comment;
import com.leduongw01.mlisserver.model.MlisUser;
import com.leduongw01.mlisserver.model.StringValue;
import com.leduongw01.mlisserver.model.ViewMlisUser;
import com.leduongw01.mlisserver.repository.CommentRepository;
import com.leduongw01.mlisserver.repository.MlisUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class MlisUserService {
    @Autowired
    MlisUserRepository mlisUserRepository;
    @Autowired
    CommentRepository commentRepository;
    public MlisUser login(String username, String password){
        MlisUser mlisUser = new MlisUser();
        if (mlisUserRepository.existsMlisUserByUsernameAndPassword(username, password)){
            mlisUser = mlisUserRepository.getMlisUserByUsernameAndPassword(username,password);
            mlisUser.genToken();
            mlisUserRepository.save(mlisUser);
        }
        mlisUser.setPassword("hellocheater");
        return mlisUser;
    }
    public MlisUser loginwithtoken(String username, String token){
        MlisUser mlisUser = new MlisUser();
        if (mlisUserRepository.existsMlisUserByUsernameAndToken(username, token)){
            mlisUser = mlisUserRepository.getMlisUserByUsernameAndToken(username, token);
        }
        mlisUser.setPassword("hellocheater");
        return mlisUser;
    }
    public boolean register(MlisUser mlisUser){
        if (mlisUserRepository.existsByUsername(mlisUser.getUsername())){
            return false;
        }
        if (mlisUser.getUsername().trim().isEmpty() || mlisUser.getPassword().trim().isEmpty()){
            return false;
        }
        mlisUserRepository.insert(mlisUser);
        return true;
    }
    public boolean deleteUser(String userId){
        if (mlisUserRepository.existsMlisUserBy_id(userId)){
            MlisUser mlisUser = mlisUserRepository.getMlisUserBy_id(userId);
            mlisUser.setToken("-1");
            mlisUser.setStatus("-1");
            mlisUserRepository.save(mlisUser);
        }
        return true;
    }
    public boolean deleteUserAndComment(String userId){
        if (mlisUserRepository.existsMlisUserBy_id(userId)){
            MlisUser mlisUser = mlisUserRepository.getMlisUserBy_id(userId);
            mlisUser.setToken("-1");
            mlisUser.setStatus("-1");
            mlisUserRepository.save(mlisUser);
            List<Comment> commentList = commentRepository.getAllByUserIdAndStatus(userId, "1");
            for (Comment comment : commentList){
                comment.setStatus("-1");
            }
            commentRepository.saveAll(commentList);
        }
        return true;
    }
    public String getUsernameById(String userId){
        if (mlisUserRepository.existsById(userId)){
            return mlisUserRepository.getMlisUserBy_id(userId).getUsername();
        }
        return "none";
    }
    public List<StringValue> getListNameByListId(List<String> listId){
        List<StringValue> listMapName = new ArrayList<>();
        for (String userId: listId){
            if (mlisUserRepository.existsById(userId)){
                String username = mlisUserRepository.getMlisUserBy_id(userId).getUsername();
                listMapName.add(new StringValue(userId, username, "unuse"));
            }
            else{
                listMapName.add(new StringValue(userId, "Người dùng đã bị xóa", "unuse"));
            }
        }
        return listMapName;
    }
    public List<ViewMlisUser> getViewMlisUser(){
        List<MlisUser> mlisUsers = mlisUserRepository.getAllBy_idIsNotNull();
        List<ViewMlisUser> viewMlisUsers = new ArrayList<>();
        for (MlisUser mlisUser: mlisUsers){
            ViewMlisUser viewMlisUser = new ViewMlisUser();
            viewMlisUser._id = mlisUser.get_id();
            viewMlisUser.username = mlisUser.getUsername();
            viewMlisUser.email = mlisUser.getEmail();
            viewMlisUser.token = mlisUser.getToken();
            viewMlisUser.gender = mlisUser.getGender();
            viewMlisUser.dateOfBirth = mlisUser.getDateOfBirth();
            viewMlisUser.numOfComment = commentRepository.countCommentByUserId(mlisUser.get_id());
            viewMlisUser.status = mlisUser.getStatus();
            viewMlisUsers.add(viewMlisUser);
        }
        return viewMlisUsers;
    }
    public String changePass(StringValue stringValue){
        if (mlisUserRepository.existsByUsername(stringValue.getId())){
            MlisUser mlisUser = mlisUserRepository.getMlisUserByUsername(stringValue.getId());
            if (mlisUser.getStatus().equals("-1")){
                return "none";
            }
            if (stringValue.getText2().trim().equals("")){
                return "empty";
            }
            if (stringValue.getText1().equals(mlisUser.getPassword())){
                mlisUser.setPassword(stringValue.getText2());
                mlisUser.genToken();
                mlisUserRepository.save(mlisUser);
                return mlisUser.getToken();
            }
            else{
                return "notMatch";
            }
        }
        return "notFound";
    }
}
