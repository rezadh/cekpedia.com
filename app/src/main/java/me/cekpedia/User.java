package me.cekpedia;

/**
 * Created by rezadwihendarno on 06/04/2018.
 */

public class User {
    private String User;
    private String email;
    private String photoUrl;
    private String Uid;
    private String favourite;
    public User(String user, String email, String photoUrl, String uid, String favourite) {
        this.User = user;
        this.email = email;
        this.photoUrl = photoUrl;
        this.Uid = uid;
        this.favourite = favourite;
    }
    public User(String user, String email, String photoUrl, String uid) {
        this.User = user;
        this.email = email;
        this.photoUrl = photoUrl;
        this.Uid = uid;
    }

    public String getFavourite() {
        return favourite;
    }

    public void setFavourite(String favourite) {
        this.favourite = favourite;
    }

    //
//    public User(String email, boolean favourite, String uid) {
//        this.email = email;
//        this.favourite = favourite;
//
//        Uid = uid;
//    }
    public User(){

    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }
}
