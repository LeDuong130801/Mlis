package com.leduongw01.mlisserver.service;

import com.leduongw01.mlisserver.model.Podcast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.Random;

@Service
public class MediaStoragedService {
    private final Path fileStorageLocation;

    @Autowired
    public MediaStoragedService(Environment env) {
        this.fileStorageLocation = Paths.get(env.getProperty("app.file.upload-dir", "./uploads/files"))
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null) {
            return null;
        }
        String[] fileNameParts = fileName.split("\\.");

        return fileNameParts[fileNameParts.length - 1];
    }

    public String storeFile(Podcast podcast, MultipartFile file) {
        Random r = new Random();
        String fileName =
                r.nextLong()+podcast.getName()+ podcast.getCreateBy()+podcast.getCreateOn()+file.getName()+"." + getFileExtension(file.getOriginalFilename());
        try {
            // Check if the filename contains invalid characters
            if (fileName.contains("..")) {
                throw new RuntimeException(
                        "Duoi file khong xac dinh " + fileName);
            }
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Luu file that bai", ex);
        }
    }
    public String storeImage(Podcast podcast, MultipartFile image) {
        Random r = new Random();
        String fileName =
                r.nextLong()+podcast.getName()+ podcast.getCreateBy()+podcast.getCreateOn()+image.getName()+"." + getFileExtension(image.getOriginalFilename());
        try {
            // Check if the filename contains invalid characters
            if (fileName.contains("..")) {
                throw new RuntimeException(
                        "Duoi anh khong xac dinh " + fileName);
            }
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(image.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Luu anh that bai", ex);
        }
    }
}
