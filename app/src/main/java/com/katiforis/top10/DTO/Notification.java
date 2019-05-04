package com.katiforis.top10.DTO;

import java.io.Serializable;


public class Notification implements Serializable {
    private long id;
    private String message;
    private String date;
    private boolean isNew;

    public Notification() {
    }

    public Notification(String message, String date) {
        this.message = message;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }
}
