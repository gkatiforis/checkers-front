package com.katiforis.top10.controller;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.katiforis.top10.conf.gson.DateTypeAdapter;
import com.katiforis.top10.stomp.Client;

import java.util.Date;

import io.reactivex.Flowable;
import ua.naiksoftware.stomp.dto.StompMessage;

import static com.katiforis.top10.conf.Const.TAG;


public abstract class AbstractController {
    protected final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, DateTypeAdapter.getAdapter())
            .create();
    public void addTopic(String topicId) {
        if(topicId == null || topicId.isEmpty())
            return;
        Flowable<StompMessage> messageFlowable = Client.getInstance().addTopic(getControllerId(), topicId);
        if(messageFlowable != null)
            messageFlowable.subscribe(this::onReceive, throwable ->
                Log.e(TAG, "Error on subscribe topic", throwable)
        );
    }

    protected abstract void onReceive(StompMessage stompMessage);

    protected String getControllerId(){
        return this.getClass().getName();
    }
}
