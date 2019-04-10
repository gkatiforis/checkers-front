package com.katiforis.top10.DTO;

import java.io.Serializable;

public abstract class Game implements Serializable {
    protected String status;
    protected String gameId;
    protected String userId;

    public Game(String status) {
        this.status = status;
    }

    public Game(String gameId, String status) {
        this.gameId = gameId;
        this.status = status;
    }

    public Game(String status, String gameId, String userId) {
        this.status = status;
        this.gameId = gameId;
        this.userId = userId;
    }
}
