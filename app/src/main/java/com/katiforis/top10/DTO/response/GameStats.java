package com.katiforis.top10.DTO.response;

import com.katiforis.top10.DTO.PlayerDto;

import java.util.List;

public class GameStats extends GameResponse {

    List<PlayerDto> players;

    public GameStats(String gameId) {
        super(ResponseState.END_GAME.getState(), gameId);
    }

    public GameStats(List<PlayerDto> players) {
        this.players = players;
    }

    public GameStats(String status, String gameId, List<PlayerDto> players) {
        super(status, gameId);
        this.players = players;
    }

    public GameStats(String gameId, String status, String gameId1, List<PlayerDto> players) {
        super(gameId, status, gameId1);
        this.players = players;
    }

    public List<PlayerDto> getPlayers() {

        return players;
    }

    public void setPlayers(List<PlayerDto> players) {
        this.players = players;
    }
}
