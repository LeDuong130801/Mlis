package com.leduongw01.mlis.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class Podcast {
    private String _id;
    private String name;
    private String inf;
    private String createOn;
    private String createBy;
    private String author;
    private String category;
    private String url;
    private String urlImg;
    private String status;

    public Podcast() {
        status = "0";
    }

    public Podcast(String _id, String name, String inf, String createOn, String createBy, String author, String category, String url, String urlImg, String status) {
        this._id = _id;
        this.name = name;
        this.inf = inf;
        this.createOn = createOn;
        this.createBy = createBy;
        this.author = author;
        this.category = category;
        this.url = url;
        this.urlImg = urlImg;
        this.status = status;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInf() {
        return inf;
    }

    public void setInf(String inf) {
        this.inf = inf;
    }

    public String getCreateOn() {
        return createOn;
    }

    public void setCreateOn(String createOn) {
        this.createOn = createOn;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
