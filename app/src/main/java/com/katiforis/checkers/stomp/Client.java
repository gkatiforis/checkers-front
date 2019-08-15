package com.katiforis.checkers.stomp;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.katiforis.checkers.activities.CheckersApplication;
import com.katiforis.checkers.activities.MenuActivity;
import com.katiforis.checkers.conf.Const;
import com.katiforis.checkers.util.LocalCache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import io.reactivex.Flowable;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.dto.StompMessage;

import static com.katiforis.checkers.conf.Const.TAG;
import static com.katiforis.checkers.util.CachedObjectProperties.TOKEN;
import static com.katiforis.checkers.util.CachedObjectProperties.USER_ID;


public class Client extends Observable {
    private static Client CLIENT_INSTANCE = null;
    private static StompClient stompClient;

    private static final Map<String, Flowable<StompMessage>> subscriptionsByTopics = new HashMap<>();
    private static final Map<String, String> topicsByControllers = new HashMap<>();
    private static final String HEADER_TOKEN = "TOKEN";
    private static final String HEADER_USER_ID = "USER_ID";
    private static final int RECONNECT_DELAY_IN_SECONDS = 2;
    private boolean reconnecting = false;
    private static boolean disconnectedFromUser = false;
    private Handler reconnectioHandler = new Handler();
    private Handler sendConnectionHandler;
    private static Handler sendMoveHandler;
    private static Handler sendGameStateHandler;


    private Client() {
    }

    public static Client getInstance() {
        if (CLIENT_INSTANCE == null) {
            synchronized (Client.class) {
                CLIENT_INSTANCE = new Client();
                CLIENT_INSTANCE.connect();
            }
        }
        return CLIENT_INSTANCE;
    }


    private void connect() {
        stompClient = null;
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Const.host_stomp);

        List<StompHeader> headers = new ArrayList<>();
        String token = LocalCache.getInstance().getString(TOKEN, CheckersApplication.getAppContext());
        String userId = LocalCache.getInstance().getString(USER_ID, CheckersApplication.getAppContext());
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
                            if (!disconnectedFromUser) {
                                onConnectionLose();
                            } else {
                                disconnectedFromUser = false;
                            }
                            break;
                    }
                }, throwable -> {
                    Log.e(TAG, "Error on sending data", throwable);
                });

        sendConnectionMessage();


    }


    public void sendConnectionMessage() {
        if (sendConnectionHandler != null) {
            sendConnectionHandler.removeCallbacksAndMessages(null);
        }
        sendConnectionHandler = new Handler(Looper.getMainLooper());
        sendConnectionHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (stompClient != null) {
                    stompClient.send(Const.KEEP_CONNECTION, "data").subscribe(
                            () -> Log.d(TAG, "Data sent"),
                            throwable -> {
                                Log.e(TAG, "Error on sending data", throwable);
                            });
                }
                sendConnectionHandler.postDelayed(this, 50000);
            }
        }, 50000);
    }

    public void disconnect() {
        disconnectedFromUser = true;
        stompClient.disconnect();
        subscriptionsByTopics.clear();
        topicsByControllers.clear();
//        NotificationFragment.populated = false;
        MenuActivity.populated = false;
        stompClient = null;
        CLIENT_INSTANCE = null;
    }

    private void onConnectionLose() {
        subscriptionsByTopics.clear();
        topicsByControllers.clear();
//        NotificationFragment.populated = false;
        MenuActivity.populated = false;

        setChanged();
        notifyObservers(false);

        if (!reconnecting) {
            reconnecting = true;
            this.tryToReconnect();
        }
    }

    private void onReconnected() {
        reconnectioHandler.removeCallbacksAndMessages(null);
        if (reconnecting) {
            reconnecting = false;
            setChanged();
            notifyObservers(true);
        }
    }

    private void tryToReconnect() {
        connect();
        reconnectioHandler.postDelayed(this::tryToReconnect, RECONNECT_DELAY_IN_SECONDS * 1000);
    }

    public static Flowable<StompMessage> addTopic(final String topicId, final boolean force) {
        if (topicId == null) {
            return null;
        }
        if (subscriptionsByTopics.get(topicId) == null || force) {
            subscriptionsByTopics.put(topicId, stompClient.topic(topicId));
            return subscriptionsByTopics.get(topicId);
        }
        return null;
    }

    public static boolean isConnected() {
        if (stompClient == null) {
            return false;
        }
        return stompClient.isConnected();
    }

    public static void clearTopics(final String controllerId) {
        topicsByControllers.remove(controllerId);
    }

    public static void send(String destination, String data) {
        stompClient.send(destination, data).subscribe(
                () -> {
                    Log.d(TAG, "Data sent");
                },
                throwable -> {
                    Log.e(TAG, "Error on sending data", throwable);
                });
    }

    public static void send(String destination) {
        stompClient.send(destination).subscribe(
                () -> Log.d(TAG, "Data sent"),
                throwable -> {
                    Log.e(TAG, "Error on sending data", throwable);
                });
    }
}
