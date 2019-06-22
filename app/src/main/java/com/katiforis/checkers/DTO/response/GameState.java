package com.katiforis.checkers.DTO.response;

import com.katiforis.checkers.DTO.UserDto;
import com.katiforis.checkers.game.Board;
import com.katiforis.checkers.game.Piece;

import java.util.Date;
import java.util.List;

public class GameState extends GameResponse {
    private List<UserDto> players;
    private Board board;
    private Date dateStarted;
    private Date currentDate;
    private Date lastMoveDate;
    private Integer gameMaxTime;
    private boolean draw;

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

    public Date getLastMoveDate() {
        return lastMoveDate;
    }

    public void setLastMoveDate(Date lastMoveDate) {
        this.lastMoveDate = lastMoveDate;
    }

    public Integer getGameMaxTime() {
        return gameMaxTime;
    }

    public void setGameMaxTime(Integer gameMaxTime) {
        this.gameMaxTime = gameMaxTime;
    }

    public UserDto getCurrentPlayer(){
        if(players.get(0).getCurrent()){
            return players.get(0);
        }else{
            return players.get(1);
        }
    }

    public UserDto getPrevPlayer(){
        if(players.get(0).getCurrent()){
            return players.get(1);
        }else{
            return players.get(0);
        }
    }

    public UserDto getWhitePlayer(){
        if(players.get(0).getColor().equals(Piece.LIGHT)){
            return players.get(0);
        }else{
            return players.get(1);
        }
    }

    public UserDto getDarkPlayer(){
        if(players.get(0).getColor().equals(Piece.DARK)){
            return players.get(0);
        }else{
            return players.get(1);
        }
    }

    public boolean isDraw() {
        return draw;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }
}
