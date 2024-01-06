package com.leduongw01.mlisserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leduongw01.mlisserver.model.Podcast;
import com.leduongw01.mlisserver.model.ViewPodcast;
import com.leduongw01.mlisserver.service.MediaStoragedService;
import com.leduongw01.mlisserver.service.PodcastService;
import io.micrometer.core.instrument.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/podcast")
public class PodcastController {
    @Autowired
    PodcastService podcastService;
    @Autowired
    MediaStoragedService mediaStoragedService;

    @PostMapping("/uploadpodcast")
    public ResponseEntity<String> uploadFile(@RequestParam(name = "file", required = false) MultipartFile file,@RequestParam(name = "image", required = false) MultipartFile image, @RequestParam(name = "podcast") String podcaststr) throws JsonProcessingException {
        Podcast podcast = new ObjectMapper().readValue(podcaststr, Podcast.class);
        Podcast newPod = podcastService.addPodcast(podcast);
        String fileName = mediaStoragedService.storeFile(newPod, file);
        newPod.setUrl("http:\\\\192.168.1.35:8080\\storage\\files\\"+fileName);
        String imageName = mediaStoragedService.storeImage(newPod, image);
        newPod.setUrlImg("http:\\\\192.168.1.35:8080\\storage\\files\\"+imageName);
        podcastService.updatePodcast(newPod);
        return ResponseEntity.ok().body(fileName);
    }
    @PutMapping("/updatefilepodcast")
    public ResponseEntity<String> updateFile(@RequestParam(name = "file", required = false) MultipartFile file, @RequestParam(name = "image", required = false) MultipartFile image, @RequestParam(name = "podcast") String podcastStr){
        try {
            Podcast podcast = new ObjectMapper().readValue(podcastStr, Podcast.class);
            if (file!=null){
                String fileName = mediaStoragedService.storeFile(podcast, file);
                podcast.setUrl("http:\\\\192.168.1.35:8080\\storage\\files\\"+fileName);
            }
            if (image!=null){
                String imageName = mediaStoragedService.storeImage(podcast, image);
                podcast.setUrlImg("http:\\\\192.168.1.35:8080\\storage\\files\\"+imageName);
            }
            podcastService.updatePodcast(podcast);
            return ResponseEntity.ok().body("ok");
        } catch (JsonProcessingException e) {
            log.error("json ex");
            return ResponseEntity.badRequest().body("json ex");
        }
    }
    @GetMapping("/getpodcastwithsl")
    public List<Podcast> getAllPodcastWithSl(@RequestParam(value = "page") Integer page, @RequestParam("quantity") Integer quantity){
        log.info("sl"+page);
        return podcastService.getPodcastBySl(page, quantity);
    }
    public ResponseEntity<Podcast> getPodcastById(String id){
        return ResponseEntity.ok().body(podcastService.getPodcastById(id));
    }
    @GetMapping("/getall")
    public List<Podcast> getAll(){
        return podcastService.getAll();
    }
    @GetMapping("/getallview")
    public List<ViewPodcast> getAllView(){
        return podcastService.getAllViewPodcast();
    }
    @PostMapping("/updatePodcast")
    public Podcast updatePodcast(@RequestBody Podcast podcast){
        return podcastService.updatePodcast(podcast);
    }
    @DeleteMapping("/deletePodcast")
    public void deletePodcast(@RequestParam("podcastId")String podcastId){
        podcastService.deletePodcast(podcastId);
    }
    @GetMapping("/count")
    public String countPodcast(){ return podcastService.countPodcast(); }
}
