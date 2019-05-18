package com.katiforis.top10.DTO.response;


import com.katiforis.top10.DTO.PlayerDto;

import java.util.ArrayList;
import java.util.List;

public class Lobby extends BaseResponse {
    List<PlayerDto> players = new ArrayList<>();
    public Lobby(String status) {
        super(status);
    }

    public Lobby(String gameId, String status) {
        super(gameId, status);
    }

    public List<PlayerDto> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerDto> players) {
        this.players = players;
    }
}
