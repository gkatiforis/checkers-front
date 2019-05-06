package com.katiforis.top10.DTO.response;


import com.katiforis.top10.DTO.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationList extends BaseResponse {
    List<Notification> notifications = new ArrayList<>();
    public NotificationList(String status) {
        super(status);
    }

    public NotificationList(String gameId, String status) {
        super(gameId, status);
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }
}
