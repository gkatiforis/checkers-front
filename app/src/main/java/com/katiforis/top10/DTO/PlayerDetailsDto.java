package com.katiforis.top10.DTO;

import java.io.Serializable;

public class PlayerDetailsDto implements Serializable {

    private int elo;
    private int level;
    private int levelPoints;
    private int coins;
    private int eloExtra;
    private int levelExtra;
    private int levelPointsExtra;
    private int coinsExtra;


    public PlayerDetailsDto() {}

    public PlayerDetailsDto(int elo, int level, int levelPoints, int coins, int eloExtra, int levelExtra, int levelPointsExtra, int coinsExtra) {
        this.elo = elo;
        this.level = level;
        this.levelPoints = levelPoints;
        this.coins = coins;
        this.eloExtra = eloExtra;
        this.levelExtra = levelExtra;
        this.levelPointsExtra = levelPointsExtra;
        this.coinsExtra = coinsExtra;
    }

    public int getElo() {
        return elo;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevelPoints() {
        return levelPoints;
    }

    public void setLevelPoints(int levelPoints) {
        this.levelPoints = levelPoints;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getEloExtra() {
        return eloExtra;
    }

    public void setEloExtra(int eloExtra) {
        this.eloExtra = eloExtra;
    }

    public int getLevelExtra() {
        return levelExtra;
    }

    public void setLevelExtra(int levelExtra) {
        this.levelExtra = levelExtra;
    }

    public int getLevelPointsExtra() {
        return levelPointsExtra;
    }

    public void setLevelPointsExtra(int levelPointsExtra) {
        this.levelPointsExtra = levelPointsExtra;
    }

    public int getCoinsExtra() {
        return coinsExtra;
    }

    public void setCoinsExtra(int coinsExtra) {
        this.coinsExtra = coinsExtra;
    }
}
