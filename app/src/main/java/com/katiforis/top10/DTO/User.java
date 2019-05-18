package com.katiforis.top10.DTO;

import java.io.Serializable;

public class User implements Serializable {
    static final long serialVersionUID =-4596596853482889445L;
    private long id;
    private String username;
    private String imageUrl;

    public User(){}
    public User(String username) {
        this.username = username;
    }


    public User(long id, String username, String imageUrl) {
        this.id = id;
        this.username = username;
        this.imageUrl = imageUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
