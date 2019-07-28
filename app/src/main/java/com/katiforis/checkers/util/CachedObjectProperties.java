package com.katiforis.checkers.util;

public enum CachedObjectProperties {
    TOKEN("TOKEN", -1),
    USER_ID("USER_ID", -1),
    CURRENT_GAME_ID("CURRENT_GAME_ID", -1),
    RANK_LIST("RANK_LIST", 60*5),
    USER_DETAILS("USER_DETAILS", 60*60*24*30*12*10),
    SOUND_ENABLED("SOUND_ENABLED", -1);

    private String key;
    private int expireLimitInSeconds;

    CachedObjectProperties(String key, int expireLimitInSeconds) {
        this.key = key;
        this.expireLimitInSeconds = expireLimitInSeconds;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getExpireLimitInSeconds() {
        return expireLimitInSeconds;
    }

    public void setExpireLimitInSeconds(int expireLimitInSeconds) {
        this.expireLimitInSeconds = expireLimitInSeconds;
    }
}
