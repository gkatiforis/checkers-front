package com.katiforis.top10.DTO;

import java.io.Serializable;

public class PlayerDto extends User implements Serializable {
    private String playerId;
    private PlayerDetailsDto playerDetails;


    public PlayerDto(String username) {
        super(username);
    }
    public PlayerDto(String playerId, String username) {
        super(username);
        this.playerId = playerId;
    }

    public PlayerDto(String playerId, PlayerDetailsDto playerDetails) {
        this.playerId = playerId;
        this.playerDetails = playerDetails;
    }

    public PlayerDto(String username, String playerId, PlayerDetailsDto playerDetails) {
        super(username);
        this.playerId = playerId;
        this.playerDetails = playerDetails;
    }

    public PlayerDto(long id, String username, String imageUrl, String playerId, PlayerDetailsDto playerDetails) {
        super(id, username, imageUrl);
        this.playerId = playerId;
        this.playerDetails = playerDetails;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public PlayerDetailsDto getPlayerDetails() {
        return playerDetails;
    }

    public void setPlayerDetails(PlayerDetailsDto playerDetails) {
        this.playerDetails = playerDetails;
    }
}
