package com.katiforis.top10.controller;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.katiforis.top10.DTO.Lobby;
import com.katiforis.top10.DTO.ResponseState;
import com.katiforis.top10.conf.Const;
import com.katiforis.top10.conf.gson.DateTypeAdapter;
import com.katiforis.top10.fragment.LobbyFragment;
import com.katiforis.top10.stomp.Client;

import org.json.JSONObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Date;

import ua.naiksoftware.stomp.client.StompMessage;


public class LobbyController extends MenuController{

    private static LobbyController INSTANCE = null;

    private LobbyFragment lobbyFragment;

    private LobbyController(){ }

    public static LobbyController getInstance() {
        if (INSTANCE == null) {
            synchronized(LobbyController.class) {
                INSTANCE = new LobbyController();
            }
        }
        return INSTANCE;
    }

    public LobbyFragment getLobbyFragment() {
        return lobbyFragment;
    }

    public void setLobbyFragment(LobbyFragment lobbyFragment) {
        this.lobbyFragment = lobbyFragment;
    }

    @Override
    public void onReceive(StompMessage stompMessage){
        JsonParser jsonParser = new JsonParser();
        JsonObject jo = (JsonObject)jsonParser.parse(stompMessage.getPayload());

        Log.i(Const.TAG, "Receive: " + jo.toString());

        JsonObject message = jo.getAsJsonObject("body");
        String messageStatus =  message.get("status").getAsString();

        Log.i(Const.TAG, "Receive: " + messageStatus);
         if(messageStatus.equalsIgnoreCase(ResponseState.LOBBY.getState())){
            final Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Date.class, DateTypeAdapter.getAdapter())
                    .create();

            Lobby lobby = gson.fromJson(message, Lobby.class);
            lobbyFragment.populateLobby(lobby);
        }
    }

    public void getLobby(JSONObject jsonObject){
        Client.getInstance().send(Const.GET_LOBBY, jsonObject.toString()).subscribe(new Subscriber<Void>() {
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
