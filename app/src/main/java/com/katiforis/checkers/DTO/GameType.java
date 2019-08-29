package com.katiforis.checkers.DTO;

public enum GameType {
    FRIENDLY(0),
    RANKING(1);

    GameType(int fee) {
        this.fee = fee;
    }

    int fee;


    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }
}
