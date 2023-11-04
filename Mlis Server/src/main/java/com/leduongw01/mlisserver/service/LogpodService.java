package com.leduongw01.mlisserver.service;

import com.leduongw01.mlisserver.model.Logpod;
import com.leduongw01.mlisserver.repository.LogpodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogpodService {
    @Autowired
    LogpodRepository logpodRepository;
    public List<Logpod> getAllByType(String type){
        return logpodRepository.getAllByType(type);
    }
    public List<Logpod> getAllByObjectId(String objId){
        return  logpodRepository.getAllByObjectId(objId);
    }
    public Logpod getLogpodById(String id){
        return logpodRepository.getLogpodBy_id(id);
    }
    public Logpod sendLogpod(Logpod logpod){
        return logpodRepository.save(logpod);
    }
    public boolean deleteLogpodById(String id){
        if (logpodRepository.existsLogpodBy_id(id)){
            logpodRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
