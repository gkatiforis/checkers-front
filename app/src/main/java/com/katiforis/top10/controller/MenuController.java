package com.katiforis.top10.controller;

import com.katiforis.top10.activities.MenuActivity;
import com.katiforis.top10.conf.Const;
import com.katiforis.top10.stomp.Client;
import ua.naiksoftware.stomp.client.StompMessage;


public abstract class MenuController {

    private MenuActivity menuActivity;

    public void init(String userId) {
        if(userId == null)return;
        Client.getInstance()
                .addTopic(Const.MAIN_TOPIC_RESPONSE.replace(Const.placeholder, userId))
                .subscribe(message-> onReceive(message));
    }

    protected abstract void onReceive(StompMessage stompMessage);

    public MenuActivity getMenuActivity() {
        return menuActivity;
    }

    public void setMenuActivity(MenuActivity menuActivity) {
        this.menuActivity = menuActivity;
    }
}
