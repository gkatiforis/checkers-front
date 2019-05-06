package com.katiforis.top10.DTO.response;

import java.io.Serializable;

public abstract class GameResponse extends BaseResponse implements Serializable {
    protected String gameId;

    public GameResponse(){super();}
    public GameResponse(String status, String gameId) {
        super(status);
        this.gameId = gameId;
    }

    public GameResponse(String gameId, String status, String gameId1) {
        super(gameId, status);
        this.gameId = gameId1;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
