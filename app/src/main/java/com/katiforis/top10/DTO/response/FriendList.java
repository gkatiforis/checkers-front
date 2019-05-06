package com.katiforis.top10.DTO.response;


import com.katiforis.top10.DTO.Player;
import com.katiforis.top10.DTO.response.BaseResponse;

import java.util.ArrayList;
import java.util.List;

public class FriendList extends BaseResponse {
    List<Player> players = new ArrayList<>();
    public FriendList(String status) {
        super(status);
    }

    public FriendList(String gameId, String status) {
        super(gameId, status);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
