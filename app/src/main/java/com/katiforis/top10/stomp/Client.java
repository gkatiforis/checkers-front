package com.katiforis.top10.stomp;

import android.os.Handler;
import android.util.Log;

import com.katiforis.top10.activities.MenuActivity;
import com.katiforis.top10.conf.Const;
import com.katiforis.top10.fragment.NotificationFragment;
import com.katiforis.top10.util.LocalCache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.dto.StompMessage;

import static com.katiforis.top10.conf.Const.TAG;
import static com.katiforis.top10.util.CachedObjectProperties.TOKEN;
import static com.katiforis.top10.util.CachedObjectProperties.USER_ID;


public class Client {
    private static Client CLIENT_INSTANCE = null;
    private static StompClient stompClient;

    private static final Map<String, Flowable<StompMessage>> subscriptionsByTopics = new HashMap<>();
    private static final Map<String, String> topicsByControllers = new HashMap<>();
    public static final String HEADER_TOKEN = "TOKEN";
    public static final String HEADER_USER_ID = "USER_ID";
    private static final int RECONNECT_DELAY_IN_SECONDS = 10;
    private boolean reconnecting = false;
    private static boolean disconnectedFromUser = false;
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
            stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Const.host_stomp);
            List<StompHeader> headers = new ArrayList<>();
            String token = LocalCache.getInstance().getString(TOKEN);
            String userId = LocalCache.getInstance().getString(USER_ID);
            headers.add(new StompHeader(HEADER_TOKEN, token));
            headers.add(new StompHeader(HEADER_USER_ID, userId));

            stompClient.connect(headers);
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
                                if(!disconnectedFromUser){
                                    onConnectionLose();
                                }else {
                                    disconnectedFromUser = false;
                                }
                                break;
                        }
                    }, throwable -> {
                        Log.e(TAG, "Error on sending data", throwable);
                    });
    }


    public void disconnect() {
        disconnectedFromUser = true;
            stompClient.disconnect();
            subscriptionsByTopics.clear();
            topicsByControllers.clear();
            NotificationFragment.populated = false;
            MenuActivity.populated = false;
            stompClient = null;
            CLIENT_INSTANCE = null;
    }

    private void onConnectionLose() {
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

    public static void clearTopics(final String controllerId){
        try{
        topicsByControllers.remove(controllerId);
        }catch (Throwable e){
            Log.e(TAG, "Error on sending data", e);
        }
    }

    public static void send(String destination, String data) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!isConnected()) {
                    handler.postDelayed(this, 1000);
                }else{
                    stompClient.send(destination, data).subscribe(
                            () -> Log.d(TAG, "Data sent"),
                            throwable -> {
                                Log.e(TAG, "Error on sending data", throwable);
                            });
                }
            }
        }, 0);
    }

    public static void send(String destination) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!isConnected()) {
                    handler.postDelayed(this, 1000);
                }else{
                    stompClient.send(destination).subscribe(
                            () -> Log.d(TAG, "Data sent"),
                            throwable -> {
                                Log.e(TAG, "Error on sending data", throwable);
                            });
                }
            }
        }, 0);
    }

    public static boolean isConnected(){
        if(stompClient == null){
            return false;
        }
       return stompClient.isConnected();
    }
}
