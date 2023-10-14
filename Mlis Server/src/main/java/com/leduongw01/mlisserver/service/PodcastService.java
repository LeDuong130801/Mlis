package com.leduongw01.mlisserver.service;

import com.leduongw01.mlisserver.model.Podcast;
import com.leduongw01.mlisserver.repository.PodcastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PodcastService {
    Podcast nullPodcast = new Podcast("-1");
    @Autowired
    PodcastRepository podcastRepository;
    public Boolean exisPodcastById(String id){
        return podcastRepository.existsPodcastBy_id(id);
    }
    public Podcast getPodcastById(String id){
        if (podcastRepository.existsPodcastBy_id(id))
        return podcastRepository.getPodcastBy_id(id);
        else return new Podcast("-1");
    }
    public ArrayList<Podcast> getAllByCreateBy(String author){
        return podcastRepository.getAllByCreateBy(author);
    }
    public void addPodcast(Podcast podcast){
        podcastRepository.insert(podcast);
    }
//    public ArrayList<Podcast> getAllByCreateBy(String author){
//        return podcastRepository.getAllByCreateBy(author);
//    }
}
