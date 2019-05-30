package com.katiforis.top10.DTO.response;

import com.katiforis.top10.DTO.UserDto;

import java.util.List;

public class GameStats extends GameResponse {

    List<UserDto> players;

    public GameStats(String gameId) {
        super(ResponseState.END_GAME.getState(), gameId);
    }

    public GameStats(List<UserDto> players) {
        this.players = players;
    }

    public GameStats(String status, String gameId, List<UserDto> players) {
        super(status, gameId);
        this.players = players;
    }

    public GameStats(String gameId, String status, String gameId1, List<UserDto> players) {
        super(gameId, status, gameId1);
        this.players = players;
    }

    public List<UserDto> getPlayers() {

        return players;
    }

    public void setPlayers(List<UserDto> players) {
        this.players = players;
    }
}
