package com.katiforis.checkers.DTO;

public enum RewardEnum {
    VIDEO(100);

    int coinsPrize;

    RewardEnum(int coinsPrize) {
        this.coinsPrize = coinsPrize;
    }

    public int getCoinsPrize() {
        return coinsPrize;
    }

    public void setCoinsPrize(int coinsPrize) {
        this.coinsPrize = coinsPrize;
    }
}
