package com.katiforis.top10.DTO;

import com.katiforis.top10.DTO.response.GameResponse;
import com.katiforis.top10.DTO.response.ResponseState;

public class PlayerAnswer extends GameResponse {
    private String description;
    private Integer points;
    private boolean isCorrect = false;
    private boolean hasAlreadyBeenSaid = false;
    private long questionId;
    private PlayerDto player;

    public PlayerAnswer(String status, String gameId) {
        super(status, gameId);
    }
    public PlayerAnswer(){this.status = ResponseState.ANSWER.getState();}
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

    public PlayerDto getPlayer() {
        return player;
    }

    public void setPlayer(PlayerDto player) {
        this.player = player;
    }
}
