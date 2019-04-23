package com.katiforis.top10.stomp;

import android.util.Log;

import com.katiforis.top10.conf.Const;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.WebSocket;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;
import ua.naiksoftware.stomp.client.StompMessage;


public class Client {
    private static Client CLIENT_INSTANCE = null;
    private static StompClient stompClient;

    private static final Map<String, Flowable<StompMessage>> subscriptionsByTopic = new HashMap<>();

    private Client() {
    }

    public static Client getInstance() {
        if (CLIENT_INSTANCE == null) {
            synchronized(Client.class) {
                CLIENT_INSTANCE = new Client();
                init();
            }
        }
        return CLIENT_INSTANCE;
    }

    private static void init(){
        stompClient = Stomp.over(WebSocket.class, Const.address);
        stompClient.connect();
        stompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {
                case OPENED:
                    Log.i(Const.TAG, "Connect: stomp connection opened!");
                    break;
                case ERROR:
                    Log.e(Const.TAG, "Connect: Error occured!", lifecycleEvent.getException());
                    break;
                case CLOSED:
                    Log.i(Const.TAG, "Connect: stomp connection closed!");
                    break;
            }
        });
    }

    public static void disconnect() {
        if (stompClient != null) {
            stompClient.disconnect();
            stompClient = null;
            CLIENT_INSTANCE = null;
        }
    }

    public static Flowable<StompMessage> addTopic(String topicId){
        Flowable<StompMessage> messageFlowable = subscriptionsByTopic.get(topicId);
        if(messageFlowable == null){
             messageFlowable = stompClient.topic(topicId);
             subscriptionsByTopic.put(topicId, messageFlowable);
        }
        return messageFlowable;
    }

    public static Flowable<Void> send(String destination, String data){
        return stompClient.send(destination, data);
    }
}
