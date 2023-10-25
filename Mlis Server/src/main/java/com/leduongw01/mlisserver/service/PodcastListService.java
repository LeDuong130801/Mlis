package com.leduongw01.mlisserver.service;

import com.leduongw01.mlisserver.model.PodcastList;
import com.leduongw01.mlisserver.repository.PodcastListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PodcastListService {
    @Autowired
    PodcastListRepository podcastListRepository;
    public boolean changeNamePodcastList(String podcastListId, String newName){
        if(podcastListRepository.existsPodcastListBy_id(podcastListId)){
            PodcastList tmp = podcastListRepository.getPodcastListBy_id(podcastListId);
            tmp.setName(newName);
            podcastListRepository.save(tmp);
        }
        return false;
    }
    public boolean updatePodcastList(PodcastList podcastList, String podcastListId){
        if(podcastListRepository.existsPodcastListBy_id(podcastListId)){
            podcastList.set_id(podcastListId);
            podcastListRepository.save(podcastList);
        }
        return false;
    }
}
