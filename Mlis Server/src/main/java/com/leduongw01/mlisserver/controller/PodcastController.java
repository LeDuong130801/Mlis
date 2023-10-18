package com.leduongw01.mlisserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leduongw01.mlisserver.model.Podcast;
import com.leduongw01.mlisserver.service.MediaStoragedService;
import com.leduongw01.mlisserver.service.PodcastService;
import io.micrometer.core.instrument.util.IOUtils;
import jdk.nashorn.internal.parser.JSONParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Slf4j
@RestController
@RequestMapping("/api/podcast")
public class PodcastController {
    @Autowired
    PodcastService podcastService;
    @Autowired
    MediaStoragedService mediaStoragedService;

    @PostMapping("/uploadpodcast")
    public ResponseEntity<String> uploadFile(@RequestParam(name = "file", required = false) MultipartFile file, @RequestParam(name = "podcast") String podcaststr) throws JsonProcessingException {
        Podcast podcast = new ObjectMapper().readValue(podcaststr, Podcast.class);
        String fileName = mediaStoragedService.storeFile(podcast, file);
        podcast.setUrl("storage\\media\\"+fileName);
        podcastService.addPodcast(podcast);
        return ResponseEntity.ok().body(fileName);
    }
    public ResponseEntity<Podcast> getPodcastById(String id){
        return ResponseEntity.ok().body(podcastService.getPodcastById(id));
    }
    @GetMapping(
            value = "/get-file",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<Resource> downloadDocument(@RequestParam(name = "url") String url) throws IOException {
        InputStreamResource resource = new InputStreamResource(new FileInputStream(url));

        return ResponseEntity.ok()
                .headers(HttpHeaders.EMPTY)
                .contentLength(url.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
    @GetMapping(
            value = "/g",
            produces = MediaType.ALL_VALUE
    )
    public MultipartFile downloadDocument() throws IOException {
        InputStreamResource resource = new InputStreamResource(new FileInputStream("./storage/files/Media2localnow.mp3"));
        log.info("ĐÁadsadsads");
        return new MultipartFile() {
            @Override
            public String getName() {
                return "ahahaha";
            }

            @Override
            public String getOriginalFilename() {
                return null;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return 0;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return new byte[0];
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return resource.getInputStream();
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {

            }
        };
    }
}