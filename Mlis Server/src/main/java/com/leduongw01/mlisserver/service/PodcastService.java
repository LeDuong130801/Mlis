package com.leduongw01.mlisserver.service;

import com.leduongw01.mlisserver.model.Podcast;
import com.leduongw01.mlisserver.repository.PodcastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class PodcastService {
    Podcast nullPodcast = new Podcast("-1");
    String podcastCollection = "Podcast";
    @Autowired
    PodcastRepository podcastRepository;
    public Boolean existPodcastById(String id){
        return podcastRepository.existsPodcastBy_id(id);
    }
    public Podcast getPodcastById(String id){
        if (podcastRepository.existsPodcastBy_id(id))
        return podcastRepository.getPodcastBy_id(id);
        else return new Podcast("-1");
    }
    public List<Podcast> getAllByCreateBy(String author){
        return podcastRepository.getAllByCreateBy(author);
    }
    public List<Podcast> getAllByAuthor(String author){
        return podcastRepository.getAllByAuthor(author);
    }
    public void addPodcast(Podcast podcast){
        podcastRepository.insert(podcast);
    }
//    public String savePodcastF(Podcast podcast) throws ExecutionException, InterruptedException {
//        Firestore db = FirestoreClient.getFirestore();
//        ApiFuture<WriteResult> c = db.collection(podcastCollection).document(podcast.getName()).set(podcast);
//        return c.get().getUpdateTime().toString();
//    }
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
//    public String savePodcastFile(MultipartFile file) throws ExecutionException, InterruptedException {
//        StorageReference db = FirestoreClient.getFirestore();
//        ApiFuture<WriteResult> c = db.collection(podcastCollection).document(podcast.getName()).set(podcast);
//        return c.get().getUpdateTime().toString();
//    }
//    public ArrayList<Podcast> getAllByCreateBy(String author){
//        return podcastRepository.getAllByCreateBy(author);
//    }
}
