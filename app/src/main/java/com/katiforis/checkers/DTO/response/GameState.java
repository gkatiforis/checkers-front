package com.katiforis.checkers.DTO.response;

import com.katiforis.checkers.DTO.UserDto;
import com.katiforis.checkers.game.Board;

import java.util.Date;
import java.util.List;

public class GameState extends GameResponse {
    private List<UserDto> players;
    private UserDto currentPlayer;
    private Board board;
    private Date dateStarted;
    private Date currentDate;

    public GameState(){
        super();
    }
    public List<UserDto> getPlayers() {
        return players;
    }

    public void setPlayers(List<UserDto> players) {
        this.players = players;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Date getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(Date dateStarted) {
        this.dateStarted = dateStarted;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public UserDto getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(UserDto currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}
