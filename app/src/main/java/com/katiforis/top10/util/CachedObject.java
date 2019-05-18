package com.katiforis.top10.util;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class CachedObject<T> implements Serializable {
    static final long serialVersionUID =-1396591853482889444L;
    private T object;
    private Date savedTime;

    public CachedObject(){}

    public CachedObject(T object, Date savedTime) {
        this.object = object;
        this.savedTime = savedTime;
    }

    public boolean hasExpire(long limitInSeconds) {
        Calendar now = Calendar.getInstance();
        Calendar saved = Calendar.getInstance();
        saved.setTime (this.savedTime);
        long diff = now.getTimeInMillis() - saved.getTimeInMillis();
        long diffSeconds = diff / 1000;
        return diffSeconds > limitInSeconds;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public Date getSavedTime() {
        return savedTime;
    }

    public void setSavedTime(Date savedTime) {
        this.savedTime = savedTime;
    }

    @Override
    public String toString() {
        return "CachedObject{" +
                "object=" + object +
                ", savedTime=" + savedTime +
                '}';
    }
}