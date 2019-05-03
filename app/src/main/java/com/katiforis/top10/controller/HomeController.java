package com.katiforis.top10.controller;

import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.katiforis.top10.DTO.FriendList;
import com.katiforis.top10.DTO.GameState;
import com.katiforis.top10.DTO.PlayerDetails;
import com.katiforis.top10.DTO.ResponseState;
import com.katiforis.top10.activities.GameActivity;
import com.katiforis.top10.activities.MenuActivity;
import com.katiforis.top10.conf.Const;
import com.katiforis.top10.conf.gson.DateTypeAdapter;
import com.katiforis.top10.fragment.HomeFragment;
import com.katiforis.top10.stomp.Client;

import org.json.JSONObject;

import java.util.Date;

import ua.naiksoftware.stomp.dto.StompMessage;


public class HomeController extends MenuController{

    private static HomeController INSTANCE = null;

    private HomeFragment homeFragment;

    private MenuActivity menuActivity;

    private HomeController(){}

    public static HomeController getInstance() {
        if (INSTANCE == null) {
            synchronized(HomeController.class) {
                INSTANCE = new HomeController();
            }
        }
        return INSTANCE;
    }

     public MenuActivity getMenuActivity() {
        return menuActivity;
    }

    public void setMenuActivity(MenuActivity menuActivity) {
        this.menuActivity = menuActivity;
    }

    public void setHomeFragment(HomeFragment homeFragment) {
        this.homeFragment = homeFragment;
    }

    @Override
    public void onReceive(StompMessage stompMessage){
        JsonParser jsonParser = new JsonParser();
        JsonObject jo = (JsonObject)jsonParser.parse(stompMessage.getPayload());

        Log.i(Const.TAG, "Receive: " + jo.toString());

        JsonObject message = jo.getAsJsonObject("body");
        String messageStatus =  message.get("status").getAsString();

        Log.i(Const.TAG, "Receive: " + messageStatus);
        if(messageStatus.equalsIgnoreCase(ResponseState.START_GAME.getState())){
            Intent intent = new Intent();
            GameActivity.saveGameId(message.get("gameId").getAsString());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setClass( MenuActivity.getAppContext(), GameActivity.class);
            MenuActivity.getAppContext().startActivity(intent);
        }else if(messageStatus.equalsIgnoreCase(ResponseState.GAME_STATE.getState())){
            final Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Date.class, DateTypeAdapter.getAdapter())
                    .create();
            GameState gameState = gson.fromJson(message, GameState.class);
            GameActivity.INSTANCE.setGameState(gameState);
        }else if(messageStatus.equalsIgnoreCase(ResponseState.PLAYER_DETAILS.getState())){
            final Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Date.class, DateTypeAdapter.getAdapter())
                    .create();

            PlayerDetails playerDetails = gson.fromJson(message,PlayerDetails.class);
            homeFragment.populatePlayerDetails(playerDetails);
        }
        else if(messageStatus.equalsIgnoreCase(ResponseState.FRIEND_LIST.getState())){
            final Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Date.class, DateTypeAdapter.getAdapter())
                    .create();

            FriendList friendList = gson.fromJson(message,FriendList.class);
            getMenuActivity().populateFriendListDialog(friendList);
        }

    }

    public void findGame(JSONObject jsonObject){
        addTopic(MenuActivity.userId);
        Client.getInstance().send(Const.FIND_GAME, jsonObject.toString());
    }

    public void login(JSONObject jsonObject){
        addTopic(MenuActivity.userId);
        Client.getInstance().send(Const.LOGIN, jsonObject.toString());
    }

    public void getFriendList(JSONObject jsonObject) {
        addTopic(MenuActivity.userId);
        Client.getInstance().send(Const.GET_FRIEND_LIST, jsonObject.toString());
    }
}
