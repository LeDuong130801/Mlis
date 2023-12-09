package com.leduongw01.mlis.models;

public class ViewComment {
     String _id;
     String podcastId;
     String userId;
     String username;
     String content;
     String cmtOn;
     String status;

    public ViewComment() {
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
