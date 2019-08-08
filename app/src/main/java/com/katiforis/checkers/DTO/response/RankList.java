package com.katiforis.checkers.DTO.response;


import com.katiforis.checkers.DTO.UserDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RankList extends BaseResponse implements Serializable{
    static final long serialVersionUID =-1396591853482889445L;
    private List<UserDto> players = new ArrayList<>();
    private UserDto currentPlayer;
    private long currentPlayerPosition;
    private Date timestamp;

    public RankList(String status) {
        super(status);
    }

    public RankList(String gameId, String status) {
        super(gameId, status);
    }

    public List<UserDto> getPlayers() {
        return players;
    }

    public void setPlayers(List<UserDto> players) {
        this.players = players;
    }

    public UserDto getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(UserDto currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public long getCurrentPlayerPosition() {
        return currentPlayerPosition;
    }

    public void setCurrentPlayerPosition(long currentPlayerPosition) {
        this.currentPlayerPosition = currentPlayerPosition;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
