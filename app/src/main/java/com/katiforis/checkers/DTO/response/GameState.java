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
    private Date gameEndDate;
    private Integer gameMaxTime;
    private String offerDrawUserId;
    private Date offerDrawDate;
    private boolean draw;
    private int gameStatus;
    private GameStats gameStats;

    public static class Status {
        public static final int PLAYERS_SELECTION = 1;
        public static final int IN_PROGRESS = 2;
        public static final int TERMINATED = 3;
    }

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

    public Date getGameEndDate() {
        return gameEndDate;
    }

    public void setGameEndDate(Date gameEndDate) {
        this.gameEndDate = gameEndDate;
    }

    public Integer getGameMaxTime() {
        return gameMaxTime;
    }

    public void setGameMaxTime(Integer gameMaxTime) {
        this.gameMaxTime = gameMaxTime;
    }

    public int getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(int gameStatus) {
        this.gameStatus = gameStatus;
    }

    public GameStats getGameStats() {
        return gameStats;
    }

    public void setGameStats(GameStats gameStats) {
        this.gameStats = gameStats;
    }

    public Date getOfferDrawDate() {
        return offerDrawDate;
    }

    public void setOfferDrawDate(Date offerDrawDate) {
        this.offerDrawDate = offerDrawDate;
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

    public String getOfferDrawUserId() {
        return offerDrawUserId;
    }

    public void setOfferDrawUserId(String offerDrawUserId) {
        this.offerDrawUserId = offerDrawUserId;
    }
}
