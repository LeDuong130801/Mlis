package com.leduongw01.mlisserver.service;

import com.leduongw01.mlisserver.model.MlisUser;
import com.leduongw01.mlisserver.model.StringValue;
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
    public MlisUser login(String username, String password){
        MlisUser mlisUser = new MlisUser();
        mlisUser.set_id("-1");
        mlisUser.setPassword("eeeeeeeee");
        if (mlisUserRepository.existsMlisUserByUsernameAndPassword(username, password)){
            mlisUser = mlisUserRepository.getMlisUserByUsernameAndPassword(username,password);
            mlisUser.setToken((new Date().getTime())+"a"+new Random().nextLong());
            mlisUserRepository.save(mlisUser);

        }
        return mlisUser;
    }
    public MlisUser loginwithtoken(String username, String token){
        MlisUser mlisUser = new MlisUser();
        mlisUser.set_id("-1");
        mlisUser.setPassword("eeeeeeeee");
        if (mlisUserRepository.existsMlisUserByUsernameAndToken(username, token)){
            mlisUser = mlisUserRepository.getMlisUserByUsernameAndToken(username, token);
        }
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
}
