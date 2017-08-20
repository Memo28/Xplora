package com.example.memovaradegante.xploraapp.models;

/**
 * Created by Memo Vara De Gante on 19/08/2017.
 */

public class Spinner  {

    public String cost;
    public Integer coin_Image;

    public Spinner(String cost, Integer coin_Image) {
        this.cost = cost;
        this.coin_Image = coin_Image;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public Integer getCoin_Image() {
        return coin_Image;
    }

    public void setCoin_Image(Integer coin_Image) {
        this.coin_Image = coin_Image;
    }
}
