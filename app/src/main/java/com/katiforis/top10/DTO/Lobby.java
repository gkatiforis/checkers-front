package com.katiforis.top10.DTO;


import java.util.ArrayList;
import java.util.List;

public class Lobby extends Game {
    List<Player> players = new ArrayList<>();
    public Lobby(String status) {
        super(status);
    }

    public Lobby(String gameId, String status) {
        super(gameId, status);
    }

    public Lobby(String status, String gameId, String userId) {
        super(status, gameId, userId);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
