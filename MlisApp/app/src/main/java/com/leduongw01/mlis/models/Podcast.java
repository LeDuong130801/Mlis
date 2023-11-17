package com.leduongw01.mlis.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class Podcast {
    private String _id;
    private String name;
    private String detail;
    private String createOn;
    private String updateOn;
    private String url;
    private String urlImg;
    private String playlistId;
    private String status;

    public Podcast() {this._id = "";
        this.name = "";
        this.detail = "";
        this.createOn = "";
        this.updateOn = "";
        this.url = "";
        this.urlImg = "";
        this.playlistId = "";
        this.status = "";
    }

    public Podcast(String _id, String name, String detail, String createOn, String updateOn, String url, String urlImg, String playlistId, String status) {
        this._id = _id;
        this.name = name;
        this.detail = detail;
        this.createOn = createOn;
        this.updateOn = updateOn;
        this.url = url;
        this.urlImg = urlImg;
        this.playlistId = playlistId;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
