package com.katiforis.top10.DTO;


public class PlayerAnswer {
    private String description;
    private Integer points;
    private boolean isCorrect = false;
    private boolean hasAlreadyBeenSaid = false;
    private long questionId;
    private Player player;
    private String userId;

    public String getDescription() {
        return description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public boolean isHasAlreadyBeenSaid() {
        return hasAlreadyBeenSaid;
    }

    public void setHasAlreadyBeenSaid(boolean hasAlreadyBeenSaid) {
        this.hasAlreadyBeenSaid = hasAlreadyBeenSaid;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
