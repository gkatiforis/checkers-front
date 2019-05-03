package com.katiforis.top10.stomp;

import android.os.Handler;
import android.util.Log;

import com.katiforis.top10.activities.MenuActivity;
import com.katiforis.top10.conf.Const;
import com.katiforis.top10.controller.NotificationController;
import com.katiforis.top10.fragment.NotificationFragment;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompMessage;

import static com.katiforis.top10.conf.Const.TAG;


public class Client {
    private static Client CLIENT_INSTANCE = null;
    private static StompClient stompClient;

    private static final Map<String, Flowable<StompMessage>> subscriptionsByTopics = new HashMap<>();
    private static final Map<String, String> topicsByControllers = new HashMap<>();

    private static final int RECONNECT_DELAY_IN_SECONDS = 10;
    private boolean reconnecting = false;
    private Handler handler = new Handler();

    private Client() {}

    public static Client getInstance() {
        if (CLIENT_INSTANCE == null) {
            synchronized(Client.class) {
                CLIENT_INSTANCE = new Client();
                CLIENT_INSTANCE.initializeConnection();
            }
        }
        return CLIENT_INSTANCE;
    }

    private void initializeConnection(){
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Const.address);
        stompClient.connect();
        stompClient.lifecycle()
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            Log.i(TAG, "Connection OPENED");
                           onReconnected();
                            break;
                        case ERROR:
                            Log.e(TAG, "Connection error", lifecycleEvent.getException());
                            break;
                        case CLOSED:
                            Log.i(TAG, "Connection CLOSED");
                            onClose();
                            break;
                    }
                });
    }

    private void onClose() {
        subscriptionsByTopics.clear();
        topicsByControllers.clear();
        NotificationFragment.populated = false;
        MenuActivity.populated = false;
        MenuActivity.INSTANCE.showNoInternetDialog(true);
        this.tryToReconnect();
    }

    private void reconnect(){
        if( stompClient != null){
            stompClient.connect();
        }
    }

    private void onReconnected() {
        if(reconnecting){
            reconnecting = false;
            handler.removeCallbacks(this::reconnect);
            MenuActivity.INSTANCE.showNoInternetDialog(false);
            NotificationController.getInstance().getNotificationList();
        }
    }

    private void tryToReconnect() {
        reconnecting = true;
        handler.postDelayed(this::reconnect, RECONNECT_DELAY_IN_SECONDS * 1000);
    }

    public static Flowable<StompMessage> addTopic(final String controllerId, final String topicId){
        if(controllerId == null || topicId == null){
            return null;
        }

        if (subscriptionsByTopics.get(topicId) == null) {
            subscriptionsByTopics.put(topicId,stompClient.topic(topicId));
        }

        if(topicsByControllers.get(controllerId) == null) {
            topicsByControllers.put(controllerId, topicId);
            return subscriptionsByTopics.get(topicId);
        }else{
            return null;
        }
    }

    public static void send(String destination, String data){
         stompClient.send(destination, data).subscribe(
                 ()->  Log.d(TAG, "Data sent"),
                 throwable -> {
                        Log.e(TAG, "Error on sending data", throwable);
                    });
    }
}
