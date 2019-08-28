package com.katiforis.checkers.DTO;

public enum LevelEnum {

    LEVEL_1(100),
    LEVEL_2(200),
    LEVEL_3(300),
    LEVEL_4(400),
    LEVEL_5(500),
    LEVEL_6(700);

    int eloLimit;

    LevelEnum(int eloLimit) {
        this.eloLimit = eloLimit;
    }

    public int getEloLimit() {
        return eloLimit;
    }

    public static LevelEnum mapPointsToLevel(int points){
        if(points < LEVEL_1.getEloLimit())
            return LEVEL_1;
        if(points < LEVEL_2.getEloLimit())
            return LEVEL_2;
        if(points < LEVEL_3.getEloLimit())
            return LEVEL_3;
        if(points < LEVEL_4.getEloLimit())
            return LEVEL_4;
        if(points < LEVEL_5.getEloLimit())
            return LEVEL_5;
        return LEVEL_6;
    }
}
