package com.katiforis.top10.DTO;

import com.katiforis.top10.R;

import java.io.Serializable;


public class Player implements Serializable {
    private long id;
    private String playerId;
    private String username;
    private String fullName;
    private Integer points;
    private int img;//TODO image

    public Player() {
    }

    public Player(long id, String playerId, String username, String fullName, Integer points, int img) {
        this.id = id;
        this.playerId = playerId;
        this.username = username;
        this.fullName = fullName;
        this.points = points;
        this.img = img;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
