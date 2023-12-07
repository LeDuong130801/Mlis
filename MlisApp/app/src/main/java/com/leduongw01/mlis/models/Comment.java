package com.leduongw01.mlis.models;

public class Comment {
    String _id;
    String podcastId;
    String mlisUserId;
    String content;
    String cmtOn;
    String status;

    public Comment() {
    }
    public Comment(String podcastId, String mlisUserId, String content){
        this._id = null;
        this.podcastId = podcastId;
        this.mlisUserId = mlisUserId;
        this.content = content;
        this.cmtOn = "0";
        this.status = "1";
    }

    public Comment(String _id, String podcastId, String mlisUserId, String content, String cmtOn, String status) {
        this._id = _id;
        this.podcastId = podcastId;
        this.mlisUserId = mlisUserId;
        this.content = content;
        this.cmtOn = cmtOn;
        this.status = status;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getPodcastId() {
        return podcastId;
    }

    public void setPodcastId(String podcastId) {
        this.podcastId = podcastId;
    }

    public String getMlisUserId() {
        return mlisUserId;
    }

    public void setMlisUserId(String mlisUserId) {
        this.mlisUserId = mlisUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCmtOn() {
        return cmtOn;
    }

    public void setCmtOn(String cmtOn) {
        this.cmtOn = cmtOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
