package com.katiforis.checkers.DTO.request;


public class GetNotifications extends BaseRequest {
    long from;

    public GetNotifications(){}
    public GetNotifications(long from) {
        this.from = from;
    }

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }
}
