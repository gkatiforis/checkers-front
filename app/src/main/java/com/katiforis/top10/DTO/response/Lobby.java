package com.katiforis.top10.DTO.response;


import com.katiforis.top10.DTO.Player;

import java.util.ArrayList;
import java.util.List;

public class Lobby extends BaseResponse {
    List<Player> players = new ArrayList<>();
    public Lobby(String status) {
        super(status);
    }

    public Lobby(String gameId, String status) {
        super(gameId, status);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
