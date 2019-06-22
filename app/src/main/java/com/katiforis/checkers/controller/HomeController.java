package com.katiforis.checkers.controller;

import android.content.Intent;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.katiforis.checkers.DTO.UserDto;
import com.katiforis.checkers.DTO.request.FindGame;
import com.katiforis.checkers.DTO.response.GameState;
import com.katiforis.checkers.DTO.response.FriendList;
import com.katiforis.checkers.DTO.response.ResponseState;
import com.katiforis.checkers.DTO.response.UserStats;
import com.katiforis.checkers.activities.GameActivity;
import com.katiforis.checkers.activities.MenuActivity;
import com.katiforis.checkers.conf.Const;
import com.katiforis.checkers.fragment.HomeFragment;
import com.katiforis.checkers.stomp.Client;
import com.katiforis.checkers.util.LocalCache;
import ua.naiksoftware.stomp.dto.StompMessage;

import static com.katiforis.checkers.util.CachedObjectProperties.CURRENT_GAME_ID;
import static com.katiforis.checkers.util.CachedObjectProperties.USER_DETAILS;
import static com.katiforis.checkers.util.CachedObjectProperties.USER_ID;


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
            LocalCache.getInstance().saveString(CURRENT_GAME_ID, message.get("gameId").getAsString());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setClass( MenuActivity.getAppContext(), GameActivity.class);
            MenuActivity.getAppContext().startActivity(intent);
        }else if(messageStatus.equalsIgnoreCase(ResponseState.GAME_STATE.getState())){
           GameState gameState = gson.fromJson(message, GameState.class);
            GameActivity.INSTANCE.setGameState(gameState);
        }else if(messageStatus.equalsIgnoreCase(ResponseState.USER_STATS.getState())){
            UserStats userStats = gson.fromJson(message, UserStats.class);
            if(userStats.getUserDto().getEmail() == null){
                LocalCache.getInstance().saveString(USER_ID, userStats.getUserDto().getUserId());
            }else{
                LocalCache.getInstance().saveString(USER_ID, null);
            }
            homeFragment.populatePlayerDetails(userStats.getUserDto());
        }
        else if(messageStatus.equalsIgnoreCase(ResponseState.FRIEND_LIST.getState())){
            FriendList friendList = gson.fromJson(message,FriendList.class);
            getMenuActivity().populateFriendListDialog(friendList);
        }
    }

    public void findGame(){
        Client.clearTopics(GameController.class.getName());
        FindGame findGame = new FindGame(LocalCache.getInstance().getString(CURRENT_GAME_ID));
        addTopic(false);
        Client.send(Const.FIND_GAME, gson.toJson(findGame));
    }

    public void restartGame(){
        FindGame findGame = new FindGame(LocalCache.getInstance().getString(CURRENT_GAME_ID));
        findGame.setRestart(true);
        addTopic(false);
        Client.send(Const.FIND_GAME, gson.toJson(findGame));
    }

    public void getFriendList() {
        addTopic(false);
        Client.send(Const.GET_FRIEND_LIST);
    }

    public void getPlayerDetails(){
        UserDto playerDto = LocalCache.getInstance().get(USER_DETAILS, homeFragment.getActivity());
        if(playerDto != null){
            homeFragment.populatePlayerDetails(playerDto);
        }
        else{
            addTopic(false);
            Client.getInstance().send(Const.GET_USER_DETAILS);
        }
    }

    public void getPlayerDetailsIfExpired(){
        UserDto playerDto = LocalCache.getInstance().get(USER_DETAILS, homeFragment.getActivity());
        if(playerDto == null){
            addTopic(false);
            Client.getInstance().send(Const.GET_USER_DETAILS);
        }
    }
}
