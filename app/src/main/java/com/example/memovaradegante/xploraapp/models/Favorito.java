package com.example.memovaradegante.xploraapp.models;

/**
 * Created by Memo Vara De Gante on 03/09/2017.
 */

public class Favorito {

    public String id_favorito;
    public String id_user;
    public String title;
    public String country;
    public String imag_destiny;

    public Favorito(String id_favorito, String id_user, String title, String country, String imag_destiny) {
        this.id_favorito = id_favorito;
        this.id_user = id_user;
        this.title = title;
        this.country = country;
        this.imag_destiny = imag_destiny;
    }

    public String getId_favorito() {
        return id_favorito;
    }

    public void setId_favorito(String id_favorito) {
        this.id_favorito = id_favorito;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
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

    public String getImag_destiny() {
        return imag_destiny;
    }

    public void setImag_destiny(String imag_destiny) {
        this.imag_destiny = imag_destiny;
    }
}
