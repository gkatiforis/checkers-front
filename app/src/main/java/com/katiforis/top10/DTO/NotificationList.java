package com.katiforis.top10.DTO;


import java.util.ArrayList;
import java.util.List;

public class NotificationList extends Game {
    List<Notification> notifications = new ArrayList<>();
    public NotificationList(String status) {
        super(status);
    }

    public NotificationList(String gameId, String status) {
        super(gameId, status);
    }

    public NotificationList(String status, String gameId, String userId) {
        super(status, gameId, userId);
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }
}
