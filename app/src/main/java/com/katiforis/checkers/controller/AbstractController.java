package com.katiforis.checkers.controller;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.katiforis.checkers.conf.gson.DateTypeAdapter;
import com.katiforis.checkers.stomp.Client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import ua.naiksoftware.stomp.dto.StompMessage;

import static com.katiforis.checkers.conf.Const.TAG;

public abstract class AbstractController {
    protected final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, DateTypeAdapter.getAdapter())
            .create();

    public static List<Disposable> disposables = new ArrayList<>();

    public void addTopic(String topicId) {
        if(topicId == null || topicId.isEmpty())
            return;

        Flowable<StompMessage> messageFlowable = Client.getInstance().addTopic(topicId);
        if(messageFlowable != null){
            Disposable d =  messageFlowable.subscribe(this::onReceive, throwable -> Log.e(TAG, "Error on subscribe topic", throwable));
            disposables.add(d);
        }
    }

    public static void disposeAll(){
        for(Disposable disposable:disposables){
            disposable.dispose();
        }
        disposables.clear();
    }
    protected abstract void onReceive(StompMessage stompMessage);
}

