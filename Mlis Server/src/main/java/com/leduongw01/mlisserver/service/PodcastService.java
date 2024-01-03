package com.leduongw01.mlisserver.service;

import com.leduongw01.mlisserver.model.Playlist;
import com.leduongw01.mlisserver.model.Podcast;
import com.leduongw01.mlisserver.model.ViewPodcast;
import com.leduongw01.mlisserver.repository.PlaylistRepository;
import com.leduongw01.mlisserver.repository.PodcastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class PodcastService {
    Podcast nullPodcast = new Podcast("-1");
    String podcastCollection = "Podcast";
    @Autowired
    PodcastRepository podcastRepository;
    @Autowired
    PlaylistRepository playlistRepository;
    public Boolean existPodcastById(String id){
        return podcastRepository.existsPodcastBy_id(id);
    }
    public Podcast getPodcastById(String id){
        if (podcastRepository.existsPodcastBy_id(id))
        return podcastRepository.getPodcastBy_id(id);
        else return new Podcast("-1");
    }
    public void addPodcast(Podcast podcast){
        podcast.setCreateOn((new Date()).getTime()+"");
        podcast.setUpdateOn((new Date()).getTime()+"");
        podcastRepository.insert(podcast);
    }
    public List<Podcast> getPodcastBySl(Integer page, Integer quantity){
        List<Podcast> all = podcastRepository.getAllByStatus("1");
        List<Podcast> out;
        if(page*quantity<all.size()){
            out = all.subList((page-1)*quantity, page*quantity);
        }
        else{
            out =  all.subList((page-1)*quantity, all.size());
        }
        return out;
    }
    public List<Podcast> getAll(){
        return podcastRepository.getAllBy_idIsNotNullOrderByStatusDesc();
    }
    public Podcast updatePodcast(Podcast podcast){
        if (podcastRepository.existsPodcastBy_id(podcast.get_id())){
            Podcast p = podcastRepository.getPodcastBy_id(podcast.get_id());
            podcast.setUrlImg(p.getUrlImg());
            podcast.setUrl(p.getUrl());
            podcast.setUpdateOn(new Date().getTime()+"");
            podcast.setCreateOn(p.getCreateOn());
            podcast.setStatus(p.getStatus());
            return podcastRepository.save(podcast);
        }
        return  null;
    }
    public void deletePodcast(String podcastId){
        if (podcastRepository.existsPodcastBy_id(podcastId)){
            Podcast p = podcastRepository.getPodcastBy_id(podcastId);
            p.setUpdateOn(new Date().getTime()+"");
            p.setStatus("-1");
            podcastRepository.save(p);
        }
    }
    public String countPodcast(){
        return podcastRepository.countPodcastByStatus("1")+":"+ podcastRepository.countPodcastByStatus("0");
    }
    public List<ViewPodcast> getAllViewPodcast(){
        List<Podcast> podcasts = getAll();
        List<ViewPodcast> viewPodcastList = new ArrayList<>();
        for(Podcast podcast: podcasts){
            Playlist playlist = playlistRepository.getPlaylistBy_id(podcast.getPlaylistId());
            ViewPodcast viewPodcast = new ViewPodcast();
            viewPodcast.set_id(podcast.get_id());
            viewPodcast.setName(podcast.getName());
            viewPodcast.setCreateOn(podcast.getCreateOn());
            viewPodcast.setUpdateOn(podcast.getUpdateOn());
            viewPodcast.setDetail(podcast.getDetail());
            viewPodcast.setStatus(podcast.getStatus());
            viewPodcast.setUrl(podcast.getUrl());
            viewPodcast.setUrlImg(podcast.getUrlImg());
            if (playlist!=null){
                viewPodcast.setPlaylistId(podcast.getPlaylistId());
                viewPodcast.setAuthor(playlist.getAuthor());
                viewPodcast.setPlaylist(playlist.getName());
                viewPodcast.setCategory(playlist.getCategory());
            }
            viewPodcastList.add(viewPodcast);
        }
        return viewPodcastList;
    }

//    public String savePodcastFile(MultipartFile file) throws ExecutionException, InterruptedException {
//        StorageReference db = FirestoreClient.getFirestore();
//        ApiFuture<WriteResult> c = db.collection(podcastCollection).document(podcast.getName()).set(podcast);
//        return c.get().getUpdateTime().toString();
//    }
//    public ArrayList<Podcast> getAllByCreateBy(String author){
//        return podcastRepository.getAllByCreateBy(author);
//    }
}
