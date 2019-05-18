package com.katiforis.top10.util;

public enum CachedObjectProperties {
    RANK_LIST("RANK_LIST", 60*5);
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
