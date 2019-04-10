package com.katiforis.top10.controller;

import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.katiforis.top10.DTO.GameState;
import com.katiforis.top10.activities.GameActivity;
import com.katiforis.top10.activities.MenuActivity;
import com.katiforis.top10.conf.Const;
import com.katiforis.top10.conf.gson.DateTypeAdapter;
import com.katiforis.top10.stomp.Client;

import org.json.JSONObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Date;

import ua.naiksoftware.stomp.client.StompMessage;


public class MenuController {

    public static void init(String userId) {
        Client.getInstance().addTopic(Const.chatResponse.replace(Const.placeholder, userId))
                .subscribe(message-> onReceive(message));
    }

    public static void onReceive(StompMessage stompMessage){
        JsonParser jsonParser = new JsonParser();
        JsonObject jo = (JsonObject)jsonParser.parse(stompMessage.getPayload());

        Log.i(Const.TAG, "Receive: " + jo.toString());

        JsonObject message = jo.getAsJsonObject("body");
        String messageStatus =  message.get("status").getAsString();

        Log.i(Const.TAG, "Receive: " + messageStatus);
        if(messageStatus.equalsIgnoreCase("start")){
            Intent intent = new Intent();
            GameActivity.saveGameId(message.get("gameId").getAsString());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setClass( MenuActivity.getAppContext(), GameActivity.class);
            MenuActivity.getAppContext().startActivity(intent);
        }else if(messageStatus.equalsIgnoreCase("gamestate")){
            final Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Date.class, DateTypeAdapter.getAdapter())
                    .create();
            GameState gameState = gson.fromJson(message,GameState.class);
            GameActivity.instance.setGameState(gameState);
        }
    }

    public static void sendFindGameRequest(JSONObject jsonObject){
        Client.getInstance().send(Const.chat, jsonObject.toString()).subscribe(new Subscriber<Void>() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.i(Const.TAG, "");
            }

            @Override
            public void onNext(Void aVoid) {

            }

            @Override
            public void onError(Throwable t) {
                Log.e(Const.TAG, "", t);
            }

            @Override
            public void onComplete() {
                Log.i(Const.TAG, "" + jsonObject.toString());
            }
        });
    }

    public static void sendLogin(JSONObject jsonObject){
        Client.getInstance().send(Const.login, jsonObject.toString()).subscribe(new Subscriber<Void>() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.i(Const.TAG, "");
            }

            @Override
            public void onNext(Void aVoid) {

            }

            @Override
            public void onError(Throwable t) {
                Log.e(Const.TAG, "", t);
            }

            @Override
            public void onComplete() {
                Log.i(Const.TAG, "" + jsonObject.toString());
            }
        });
    }
}
