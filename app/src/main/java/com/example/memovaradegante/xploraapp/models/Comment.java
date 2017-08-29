package com.example.memovaradegante.xploraapp.models;

/**
 * Created by Memo Vara De Gante on 23/08/2017.
 */

public class Comment {
    public String id;
    public String user;
    public String userID;
    public String userImage;
    public String likes;
    public String dislikes;
    public String description;


    public Comment(String id, String user, String userID, String userImage, String likes, String dislikes, String description) {
        this.id = id;
        this.user = user;
        this.userID = userID;
        this.userImage = userImage;
        this.likes = likes;
        this.dislikes = dislikes;
        this.description = description;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getDislikes() {
        return dislikes;
    }

    public void setDislikes(String dislikes) {
        this.dislikes = dislikes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
