package com.katiforis.checkers.DTO.response;


import com.katiforis.checkers.DTO.UserDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RankList extends BaseResponse implements Serializable{
    static final long serialVersionUID =-1396591853482889445L;
    List<UserDto> players = new ArrayList<>();

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
}
