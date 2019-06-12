package com.katiforis.checkers.DTO;

import java.io.Serializable;

public class Answer implements Serializable {

    private long id;

    private String description;

    private Integer points;

    private Question question;


    public Answer() {
    }

    public Answer(long id, String description) {
        this.description = description;
    }

    public Answer(long id, String description, Integer points, Question question) {
        this.id = id;
        this.description = description;
        this.points = points;
        this.question = question;
    }

    public Answer(long id, String description, Integer points) {
        this.id = id;
        this.description = description;
        this.points = points;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", points=" + points +
                ", question=" + question +
                '}';
    }
}
