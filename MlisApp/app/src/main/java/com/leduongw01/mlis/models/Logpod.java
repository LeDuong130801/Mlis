package com.leduongw01.mlis.models;

public class Logpod {
    String _id;
    String type;
    String objectId;
    String content;
    String createOn;

    public Logpod() {
        _id = "0";
        type = "N"
        objectId = "N";
        content = "Trống"
        createOn = "0";
    }

    public Logpod(String _id, String type, String objectId, String content, String createOn) {
        this._id = _id;
        this.type = type;
        this.objectId = objectId;
        this.content = content;
        this.createOn = createOn;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateOn() {
        return createOn;
    }

    public void setCreateOn(String createOn) {
        this.createOn = createOn;
    }
}
