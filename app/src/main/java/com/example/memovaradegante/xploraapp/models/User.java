package com.example.memovaradegante.xploraapp.models;

/**
 * Created by Memo Vara De Gante on 09/08/2017.
 */

public class User {
    public String id;
    public String name;
    public String email;
    public String psw;
    public String country;
    public String urlImage;
    public String infoUser;
    public String interest;


    public User(String id, String name, String email, String psw, String country,String urlImage,String infoUser,String interest) {
        this.id = id;
        this.urlImage = urlImage;
        this.name = name;
        this.email = email;
        this.psw = psw;
        this.country = country;
        this.infoUser = infoUser;
        this.interest = interest;
    }

    public User(){

    }

    public String getInfoUser() {
        return infoUser;
    }

    public void setInfoUser(String infoUser) {
        this.infoUser = infoUser;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
