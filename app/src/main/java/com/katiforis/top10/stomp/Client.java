package com.katiforis.top10.stomp;

import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.katiforis.top10.DTO.GameStateDTO;
import com.katiforis.top10.DTO.PlayerAnswerDTO;
import com.katiforis.top10.activities.GameActivity;
import com.katiforis.top10.activities.MenuActivity;
import com.katiforis.top10.conf.Const;
import com.katiforis.top10.conf.gson.DateTypeAdapter;

import org.json.JSONObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;


import java.util.Date;

import okhttp3.WebSocket;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;


public class Client {

    public static StompClient stompClient;


    public static void init(){
        stompClient = Stomp.over(WebSocket.class, Const.address);
        stompClient.connect();
        StompUtils.lifecycle(stompClient);

    }

    public static void disconnect() {
        if (stompClient != null) {
            stompClient.disconnect();
            stompClient = null;
        }
    }

    public static void initConn(String userId){
        stompClient.topic(Const.chatResponse.replace(Const.placeholder, userId)).subscribe(stompMessage -> {
            JsonParser jsonParser = new JsonParser();
            JsonObject jo = (JsonObject)jsonParser.parse(stompMessage.getPayload());

            Log.i(Const.TAG, "Receive: " + jo.toString());

            JsonObject message = jo.getAsJsonObject("body");
            String messageStatus =  message.get("status").getAsString();

            Log.i(Const.TAG, "Receive: " + messageStatus);
            if(messageStatus.equalsIgnoreCase("start")){
                Intent intent = new Intent();
                MenuActivity.saveGameId(message.get("gameId").getAsString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass( MenuActivity.getAppContext(), GameActivity.class);
                MenuActivity.getAppContext().startActivity(intent);
            }else if(messageStatus.equalsIgnoreCase("gamestate")){
                final Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Date.class, DateTypeAdapter.getAdapter())
                        .create();
                GameStateDTO gameStateDTO = gson.fromJson(message,GameStateDTO.class);
                GameActivity.instance.setGameState(gameStateDTO);
            }
        });
    }


    public static void initConnGroup(String groupId){
        stompClient.topic(Const.groupResponse.replace("placeholder", groupId))

                .subscribe(stompMessage -> {


                    JsonParser jsonParser = new JsonParser();
                    JsonObject jo = (JsonObject)jsonParser.parse(stompMessage.getPayload());

                    JsonObject message = jo.getAsJsonObject("body");
                    String messageStatus = message.get("status").getAsString();
                    //JSONObject message= messageWrapper.getJSONObject("message");

                    if(messageStatus.equalsIgnoreCase("gameover")){
                        // MenuActivity.userId = userId;
                        Intent intent = new Intent();

                        MenuActivity.saveGameId(message.get("gameId").getAsString());



                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setClass( MenuActivity.getAppContext(), MenuActivity.class);
                        MenuActivity.getAppContext().startActivity(intent);
                    }else if(messageStatus.equalsIgnoreCase("answer")){

                        Gson gson=new Gson();
                        PlayerAnswerDTO playerAnswerDTO = gson.fromJson(message,PlayerAnswerDTO.class);
                        GameActivity.instance.showAnswer(playerAnswerDTO);
                    }
                    else if(messageStatus.equalsIgnoreCase("currentTime")){
                        //GameActivity.instance.updateTime(message);
                    }




//            ActivityManager am = (ActivityManager) MenuActivity.getAppContext().getSystemService(Context.ACTIVITY_SERVICE);
//            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;


           // ((GameActivity) MenuActivity.getAppContext().getApplicationContext()).showAnswer(jsonObject);


        });
    }

    public static void sendToGroup(String gameId, Object object ){
        Gson gson = new Gson();

        String jsonInString = gson.toJson(object);
        stompClient.send( Const.groupWord.replace("placeholder", gameId), jsonInString).subscribe(new Subscriber<Void>() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.i(Const.TAG, "");
            }

            @Override
            public void onNext(Void aVoid) {

            }

            @Override
            public void onError(Throwable t) {
                Log.e(Const.TAG, "Error：", t);
            }

            @Override
            public void onComplete() {

            }
        });
    }
    public static void send(JSONObject jsonObject){
        stompClient.send(Const.chat, jsonObject.toString()).subscribe(new Subscriber<Void>() {
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
        stompClient.send(Const.login, jsonObject.toString()).subscribe(new Subscriber<Void>() {
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
    public static void sendGetGameState(String userId, String gameId){
        stompClient.send(Const.gameState, userId + "|" + gameId).subscribe(new Subscriber<Void>() {
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
                Log.i(Const.TAG, "" + userId);
            }
        });
    }




}
