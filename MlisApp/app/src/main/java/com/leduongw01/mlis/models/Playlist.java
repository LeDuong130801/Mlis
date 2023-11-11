package com.leduongw01.mlis.models;


public class Playlist {
    private String _id;
    private String name;
    private String author;
    private String category;
    private String createOn;
    private String updateOn;
    private String detail;
    private String urlImg;
    private String status;

    public Playlist() {
    }

    public Playlist(String _id, String name, String author, String category, String createOn, String updateOn, String detail, String urlImg, String status) {
        this._id = _id;
        this.name = name;
        this.author = author;
        this.category = category;
        this.createOn = createOn;
        this.updateOn = updateOn;
        this.detail = detail;
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

    public String getCreateOn() {
        return createOn;
    }

    public void setCreateOn(String createOn) {
        this.createOn = createOn;
    }

    public String getUpdateOn() {
        return updateOn;
    }

    public void setUpdateOn(String updateOn) {
        this.updateOn = updateOn;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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
