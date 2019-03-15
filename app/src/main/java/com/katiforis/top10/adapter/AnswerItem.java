package com.katiforis.top10.adapter;

public class AnswerItem {
    private long id;
    private String title, genre, year;

    public AnswerItem() {
    }

    public AnswerItem(long id, String title, String genre, String year) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.year = year;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}