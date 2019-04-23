package com.katiforis.top10.DTO;


import java.util.ArrayList;
import java.util.List;

public class FriendList extends Game {
    List<Player> players = new ArrayList<>();
    public FriendList(String status) {
        super(status);
    }

    public FriendList(String gameId, String status) {
        super(gameId, status);
    }

    public FriendList(String status, String gameId, String userId) {
        super(status, gameId, userId);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
