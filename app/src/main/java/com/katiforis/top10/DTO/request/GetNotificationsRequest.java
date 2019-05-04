package com.katiforis.top10.DTO.request;

public class GetNotificationsRequest extends BaseRequest {
    long from;

    public GetNotificationsRequest(){}
    public GetNotificationsRequest(String playerId, long from) {
        super(playerId);
        this.from = from;
    }

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }
}
