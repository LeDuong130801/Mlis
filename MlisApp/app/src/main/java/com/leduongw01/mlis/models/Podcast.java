package com.leduongw01.mlis.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class Podcast {
    String id;
    String name;
    String author;
    String url;
    String urlImage;
    String uri;

    public Podcast() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Podcast(String id, String name, String author, String url, String urlImage, String uri) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.url = url;
        this.urlImage = urlImage;
        this.uri = uri;
    }
}
