package com.katiforis.checkers.DTO;

import com.katiforis.checkers.DTO.response.GameResponse;
import com.katiforis.checkers.DTO.response.ResponseState;
import com.katiforis.checkers.game.Move;

import java.util.List;


public class PlayerAnswer extends GameResponse {
    private String description;
    private Integer points;
    private boolean isCorrect = false;
    private UserDto player;
    private Move move;
    private List<UserDto> players;

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

    public UserDto getPlayer() {
        return player;
    }

    public void setPlayer(UserDto player) {
        this.player = player;
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    public List<UserDto> getPlayers() {
        return players;
    }

    public void setPlayers(List<UserDto> players) {
        this.players = players;
    }
}
