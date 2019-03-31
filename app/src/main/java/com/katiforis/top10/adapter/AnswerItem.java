package com.katiforis.top10.adapter;

public class AnswerItem {
    private String description, username, points;
    public AnswerItem() {
    }

    public AnswerItem( String description, String username, Integer points) {
        this.description = description;
        this.username = username;
        this.points = points.toString();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}