package com.katiforis.top10.controller;

import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.katiforis.top10.DTO.PlayerAnswer;
import com.katiforis.top10.DTO.ResponseState;
import com.katiforis.top10.activities.GameActivity;
import com.katiforis.top10.activities.MenuActivity;
import com.katiforis.top10.conf.Const;
import com.katiforis.top10.stomp.Client;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import ua.naiksoftware.stomp.client.StompMessage;


public class GameController {

    public static void init(String gameId) {
        Client.getInstance().addTopic(Const.GAME_RESPONSE.replace("placeholder", gameId))
                .subscribe(message-> onReceive(message));
    }

    public static void onReceive(StompMessage stompMessage){
            JsonParser jsonParser = new JsonParser();
            JsonObject jo = (JsonObject) jsonParser.parse(stompMessage.getPayload());

            JsonObject message = jo.getAsJsonObject("body");
            String messageStatus = message.get("status").getAsString();
            //JSONObject message= messageWrapper.getJSONObject("message");

            if (messageStatus.equalsIgnoreCase(ResponseState.END_GAME.getState())) {
                // MenuActivity.userId = userId;
                Intent intent = new Intent();

                GameActivity.saveGameId(message.get("gameId").getAsString());


                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(MenuActivity.getAppContext(), MenuActivity.class);
                MenuActivity.getAppContext().startActivity(intent);
            } else if (messageStatus.equalsIgnoreCase(ResponseState.ANSWER.getState())) {

                Gson gson = new Gson();
                PlayerAnswer playerAnswer = gson.fromJson(message, PlayerAnswer.class);
                GameActivity.instance.showAnswer(playerAnswer);
            }
    }

    public static void sendAnswer(String gameId, Object object ){
        Gson gson = new Gson();
        String jsonInString = gson.toJson(object);
        Client.getInstance().send( Const.SEND_WORD.replace("placeholder", gameId), jsonInString).subscribe(new Subscriber<Void>() {
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

    public static void getGameState(String userId, String gameId){
        Client.getInstance().send(Const.GET_GAME_STATE, userId + "|" + gameId).subscribe(new Subscriber<Void>() {
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
}
