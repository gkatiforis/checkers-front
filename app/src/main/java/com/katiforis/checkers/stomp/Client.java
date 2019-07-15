package com.katiforis.checkers.stomp;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.katiforis.checkers.activities.GameActivity;
import com.katiforis.checkers.activities.MenuActivity;
import com.katiforis.checkers.conf.Const;
import com.katiforis.checkers.controller.GameController;
import com.katiforis.checkers.controller.HomeController;
import com.katiforis.checkers.fragment.NotificationFragment;
import com.katiforis.checkers.util.LocalCache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.dto.StompMessage;

import static com.katiforis.checkers.conf.Const.TAG;
import static com.katiforis.checkers.util.CachedObjectProperties.TOKEN;
import static com.katiforis.checkers.util.CachedObjectProperties.USER_ID;


public class Client {
    private static Client CLIENT_INSTANCE = null;
    private static StompClient stompClient;

    private static final Map<String, Flowable<StompMessage>> subscriptionsByTopics = new HashMap<>();
    private static final Map<String, String> topicsByControllers = new HashMap<>();
    private static final String HEADER_TOKEN = "TOKEN";
    private static final String HEADER_USER_ID = "USER_ID";
    private static final int RECONNECT_DELAY_IN_SECONDS = 10;
    private boolean reconnecting = false;
    private static boolean disconnectedFromUser = false;
    private Handler handler = new Handler();
    private Handler sendConnectionHandler;

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

        sendConnectionMessage();
    }


    public void sendConnectionMessage(){
        if(sendConnectionHandler != null){
            sendConnectionHandler.removeCallbacksAndMessages(null);
        }
        sendConnectionHandler = new Handler(Looper.getMainLooper());
        sendConnectionHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                    stompClient.send(Const.KEEP_CONNECTION, "data").subscribe(
                            () -> Log.d(TAG, "Data sent"),
                            throwable -> {
                                Log.e(TAG, "Error on sending data", throwable);
                            });
                sendConnectionHandler.postDelayed(this, 50000);

            }
        }, 50000);
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
            GameActivity.INSTANCE.showNoInternetDialog(true);
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
            GameActivity.INSTANCE.showNoInternetDialog(false);
            HomeController.getInstance().addTopic(true);
            GameController.getInstance().getGameState();
        }
    }

    private void tryToReconnect() {
        reconnecting = true;
        handler.postDelayed(this::reconnect, RECONNECT_DELAY_IN_SECONDS * 1000);
    }

    public static Flowable<StompMessage> addTopic(final String controllerId, final String topicId, final boolean force){
        if(controllerId == null || topicId == null){
            return null;
        }

        if (subscriptionsByTopics.get(topicId) == null || force) {
            subscriptionsByTopics.put(topicId,stompClient.topic(topicId));
        }

        if(topicsByControllers.get(controllerId) == null || force) {
            topicsByControllers.put(controllerId, topicId);
            return subscriptionsByTopics.get(topicId);
        }else{
            return null;
        }
    }

    public static void clearTopics(final String controllerId){
        topicsByControllers.remove(controllerId);
    }

    public static void send(String destination, String data) {
        final Handler handler = new Handler(Looper.getMainLooper());
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
                                handler.postDelayed(this, 1000);
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
