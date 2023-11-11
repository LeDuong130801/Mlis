package com.leduongw01.mlis.models;

public class MlisUser {
    private String _id;
    private String username;
    private String password;
    private String email;
    private String googleAuth;
    private String status;

    public MlisUser() {
    }

    public MlisUser(String _id, String username, String password, String email, String googleAuth, String status) {
        this._id = _id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.googleAuth = googleAuth;
        this.status = status;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGoogleAuth() {
        return googleAuth;
    }

    public void setGoogleAuth(String googleAuth) {
        this.googleAuth = googleAuth;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
