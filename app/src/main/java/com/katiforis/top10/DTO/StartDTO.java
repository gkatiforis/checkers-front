package com.katiforis.top10.DTO;

import java.io.Serializable;


public class StartDTO implements Serializable {


    private String gameId;

    public StartDTO() {
    }

    public StartDTO(String gameId) {
        this.gameId = gameId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
