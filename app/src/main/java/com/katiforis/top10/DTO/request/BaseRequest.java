package com.katiforis.top10.DTO.request;

public class BaseRequest {
    String playerId;

    public BaseRequest(){}

    public BaseRequest(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
}
