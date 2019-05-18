package com.katiforis.top10.DTO.response;


import com.katiforis.top10.DTO.PlayerDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RankList extends BaseResponse implements Serializable{
    static final long serialVersionUID =-1396591853482889445L;
    List<PlayerDto> players = new ArrayList<>();

    public RankList(String status) {
        super(status);
    }

    public RankList(String gameId, String status) {
        super(gameId, status);
    }

    public List<PlayerDto> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerDto> players) {
        this.players = players;
    }
}
