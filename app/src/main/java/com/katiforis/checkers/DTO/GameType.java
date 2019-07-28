package com.katiforis.checkers.DTO;

public enum GameType {
    FRIENDLY(0, 5, 10, 0, -5, 0, 0),
    RANKING(5, 10, 70, -5, -10, 0, 50);

    GameType(int eloExtraWin, int pointsExtraWin, int coinsExtraWin, int eloExtraLose, int pointsExtraLose, int coinsExtraLoser, int fee) {
        this.eloExtraWin = eloExtraWin;
        this.pointsExtraWin = pointsExtraWin;
        this.coinsExtraWin = coinsExtraWin;
        this.eloExtraLose = eloExtraLose;
        this.pointsExtraLose = pointsExtraLose;
        this.coinsExtraLoser = coinsExtraLoser;
        this.fee = fee;
    }

    int eloExtraWin;
    int pointsExtraWin;
    int coinsExtraWin;
    int eloExtraLose;
    int pointsExtraLose;
    int coinsExtraLoser;
    int fee;

    public int getEloExtraWin() {
        return eloExtraWin;
    }

    public void setEloExtraWin(int eloExtraWin) {
        this.eloExtraWin = eloExtraWin;
    }

    public int getPointsExtraWin() {
        return pointsExtraWin;
    }

    public void setPointsExtraWin(int pointsExtraWin) {
        this.pointsExtraWin = pointsExtraWin;
    }

    public int getCoinsExtraWin() {
        return coinsExtraWin;
    }

    public void setCoinsExtraWin(int coinsExtraWin) {
        this.coinsExtraWin = coinsExtraWin;
    }

    public int getEloExtraLose() {
        return eloExtraLose;
    }

    public void setEloExtraLose(int eloExtraLose) {
        this.eloExtraLose = eloExtraLose;
    }

    public int getPointsExtraLose() {
        return pointsExtraLose;
    }

    public void setPointsExtraLose(int pointsExtraLose) {
        this.pointsExtraLose = pointsExtraLose;
    }

    public int getCoinsExtraLoser() {
        return coinsExtraLoser;
    }

    public void setCoinsExtraLoser(int coinsExtraLoser) {
        this.coinsExtraLoser = coinsExtraLoser;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }
}
