package com.katiforis.top10.DTO;

import java.io.Serializable;

public class AnswerDTO implements Serializable {

    private long id;

    private String description;

    private Integer points;

    private QuestionDTO question;


    public AnswerDTO() {
    }

    public AnswerDTO(long id, String description) {
        this.description = description;
    }

    public AnswerDTO(long id, String description, Integer points, QuestionDTO question) {
        this.id = id;
        this.description = description;
        this.points = points;
        this.question = question;
    }

    public AnswerDTO(long id, String description, Integer points) {
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

    public QuestionDTO getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDTO question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "AnswerDTO{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", points=" + points +
                ", question=" + question +
                '}';
    }
}
