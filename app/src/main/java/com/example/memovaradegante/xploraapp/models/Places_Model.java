package com.example.memovaradegante.xploraapp.models;

/**
 * Created by Memo Vara De Gante on 20/08/2017.
 */

public class Places_Model {

    public String id;
    public String title;
    public String country;
    public String description;
    public String type;
    public String poster;
    public String cost;

    public Places_Model(){

    }

    public Places_Model(String id, String title, String country, String description, String type, String poster,String cost) {
        this.cost = cost;
        this.id=id;
        this.country = country;
        this.title = title;
        this.description = description;
        this.type = type;
        this.poster = poster;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

}
