package com.example.memovaradegante.xploraapp.models;

/**
 * Created by Memo Vara De Gante on 13/08/2017.
 */

public class Place {
    public String country;
    public String name;
    public String description;
    public String type;
    public String poster;

    public Place(String country, String name, String description, String type, String poster) {
        this.country = country;
        this.name = name;
        this.description = description;
        this.type = type;
        this.poster = poster;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
