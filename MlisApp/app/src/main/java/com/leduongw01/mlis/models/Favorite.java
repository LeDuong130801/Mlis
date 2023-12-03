package com.leduongw01.mlis.models;

import java.util.ArrayList;
import java.util.List;

public class Favorite {
    private String _id;
    private String name;
    private String createOn;
    private List<String> podListId;
    private String userId;
    private String status;

    public Favorite() {
    }

    public Favorite(String _id, String name, String createOn, List<String> podListId, String userId, String status) {
        this._id = _id;
        this.name = name;
        this.createOn = createOn;
        this.podListId = podListId;
        this.userId = userId;
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

    public String getCreateOn() {
        return createOn;
    }

    public void setCreateOn(String createOn) {
        this.createOn = createOn;
    }

    public List<String> getPodListId() {
        return podListId;
    }

    public void setPodListId(List<String> podListId) {
        this.podListId = podListId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
