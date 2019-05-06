package com.katiforis.top10.DTO.response;

public class PlayerDetails extends BaseResponse {
    private String username;
    public PlayerDetails(String status) {
        super(status);
    }

    public PlayerDetails(String status, String username) {
        super(status);
        this.username = username;
    }

    public PlayerDetails(String gameId, String status, String username) {
        super(gameId, status);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
