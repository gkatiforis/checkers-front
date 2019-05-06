package com.katiforis.top10.DTO.response;

import java.io.Serializable;

public abstract class BaseResponse implements Serializable {
    protected String status;
    protected String userId;

    public BaseResponse(){}

    public BaseResponse(String status) {
        this.status = status;
    }

    public BaseResponse(String status, String userId) {
        this.status = status;
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
