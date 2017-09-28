package com.example.memovaradegante.xploraapp.models;

import java.util.Date;

/**
 * Created by Memo Vara De Gante on 17/09/2017.
 */

public class Message {
    public String user_send;
    public String user_recibe;
    public String message;
    public long date;


    public Message(String user_send, String user_recibe, String message) {
        this.user_send = user_send;
        this.user_recibe = user_recibe;
        this.message = message;
        date = new Date().getTime();
    }

    public Message(){

    }

    public String getUser_send() {
        return user_send;
    }

    public void setUser_send(String user_send) {
        this.user_send = user_send;
    }

    public String getUser_recibe() {
        return user_recibe;
    }

    public void setUser_recibe(String user_recibe) {
        this.user_recibe = user_recibe;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
