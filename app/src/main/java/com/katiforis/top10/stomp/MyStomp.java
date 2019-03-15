package com.katiforis.top10.stomp;

import android.content.Intent;
import android.util.Log;

import com.katiforis.top10.activities.GameActivity;
import com.katiforis.top10.activities.MenuActivity;
import com.katiforis.top10.conf.Const;

import org.json.JSONObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;


import okhttp3.WebSocket;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;

public class MyStomp {

    public static  StompClient stompClient;


    public static void init(){
        stompClient = Stomp.over(WebSocket.class, Const.address);
        stompClient.connect();
        StompUtils.lifecycle(stompClient);

    }

    public static void dis() {

        if (stompClient != null) {
            stompClient.disconnect();
            stompClient = null;
        }
    }

    public static void initConn(String userId){
        stompClient.topic(Const.chatResponse.replace(Const.placeholder, userId)).subscribe(stompMessage -> {
            JSONObject jsonObject = new JSONObject(stompMessage.getPayload());
            Log.i(Const.TAG, "Receive: " + jsonObject.toString());

            JSONObject message = jsonObject.getJSONObject("body");
            String messageStatus = message.getString("status");


            if(messageStatus.equalsIgnoreCase("start")){
               // MenuActivity.userId = userId;
                Intent intent = new Intent();

                MenuActivity.saveGameId(message.getString("gameId"));

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass( MenuActivity.getAppContext(), GameActivity.class);
                MenuActivity.getAppContext().startActivity(intent);
            }else if(messageStatus.equalsIgnoreCase("gamestate")){

                GameActivity.instance.setGameState(message);
            }
        });
    }


    public static void initConnGroup(String groupId){
        stompClient.topic(Const.groupResponse.replace("placeholder", groupId))

                .subscribe(stompMessage -> {

            JSONObject jsonObject = new JSONObject(stompMessage.getPayload());



                    JSONObject message = jsonObject.getJSONObject("body");
                    String messageStatus = message.getString("status");
                    //JSONObject message= messageWrapper.getJSONObject("message");

                    if(messageStatus.equalsIgnoreCase("gameover")){
                        // MenuActivity.userId = userId;
                        Intent intent = new Intent();

                        MenuActivity.saveGameId(message.getString("gameId"));



                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setClass( MenuActivity.getAppContext(), MenuActivity.class);
                        MenuActivity.getAppContext().startActivity(intent);
                    }else if(messageStatus.equalsIgnoreCase("answer")){
                        GameActivity.instance.showAnswer(message);
                    }
                    else if(messageStatus.equalsIgnoreCase("currentTime")){
                        //GameActivity.instance.updateTime(message);
                    }




//            ActivityManager am = (ActivityManager) MenuActivity.getAppContext().getSystemService(Context.ACTIVITY_SERVICE);
//            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;


           // ((GameActivity) MenuActivity.getAppContext().getApplicationContext()).showAnswer(jsonObject);


        });
    }

    public static void sendToGroup(String gameId, JSONObject jsonObject ){
        stompClient.send( Const.groupWord.replace("placeholder", gameId), jsonObject.toString()).subscribe(new Subscriber<Void>() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.i(Const.TAG, "");
            }

            @Override
            public void onNext(Void aVoid) {

            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                Log.e(Const.TAG, "发生错误：", t);
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
