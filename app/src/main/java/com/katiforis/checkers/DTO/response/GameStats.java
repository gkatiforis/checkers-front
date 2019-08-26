package com.katiforis.checkers.DTO.response;

import com.katiforis.checkers.DTO.UserDto;

import java.util.Date;
import java.util.List;

public class GameStats extends GameResponse {
    private String winnerColor;
    private List<UserDto> players;
    private boolean draw;
    private Date gameEndDate;
    private Date currentDate;

    public GameStats(String gameId) {
        super(ResponseState.END_GAME.getState(), gameId);
    }

    public GameStats(List<UserDto> players) {
        this.players = players;
    }

    public GameStats(String status, String gameId, List<UserDto> players) {
        super(status, gameId);
        this.players = players;
    }

    public GameStats(String gameId, String status, String gameId1, List<UserDto> players) {
        super(gameId, status, gameId1);
        this.players = players;
    }

    public List<UserDto> getPlayers() {

        return players;
    }

    public void setPlayers(List<UserDto> players) {
        this.players = players;
    }

    public String getWinnerColor() {
        return winnerColor;
    }

    public void setWinnerColor(String winnerColor) {
        this.winnerColor = winnerColor;
    }

    public boolean isDraw() {
        return draw;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }

    public Date getGameEndDate() {
        return gameEndDate;
    }

    public void setGameEndDate(Date gameEndDate) {
        this.gameEndDate = gameEndDate;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }
}
