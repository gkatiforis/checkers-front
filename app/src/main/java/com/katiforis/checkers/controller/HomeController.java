package com.katiforis.checkers.controller;

import android.content.Intent;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.katiforis.checkers.DTO.UserDto;
import com.katiforis.checkers.DTO.request.FindGame;
import com.katiforis.checkers.DTO.request.GetRank;
import com.katiforis.checkers.DTO.request.Reward;
import com.katiforis.checkers.DTO.response.GameState;
import com.katiforis.checkers.DTO.response.FriendList;
import com.katiforis.checkers.DTO.response.RankList;
import com.katiforis.checkers.DTO.response.ResponseState;
import com.katiforis.checkers.DTO.response.UserStats;
import com.katiforis.checkers.activities.GameActivity;
import com.katiforis.checkers.activities.MenuActivity;
import com.katiforis.checkers.conf.Const;
import com.katiforis.checkers.stomp.Client;
import com.katiforis.checkers.util.LocalCache;

import java.util.Date;

import io.reactivex.Completable;
import ua.naiksoftware.stomp.dto.StompMessage;

import static com.katiforis.checkers.util.CachedObjectProperties.CURRENT_GAME_ID;
import static com.katiforis.checkers.util.CachedObjectProperties.RANK_LIST;
import static com.katiforis.checkers.util.CachedObjectProperties.USER_DETAILS;
import static com.katiforis.checkers.util.CachedObjectProperties.USER_ID;


public class HomeController extends MenuController{

    private static HomeController INSTANCE = null;

    private MenuActivity menuActivity;

    private GameActivity gameActivity;

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

    public GameActivity getGameActivity() {
        return gameActivity;
    }

    public void setGameActivity(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
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
            LocalCache.getInstance().saveString(CURRENT_GAME_ID, message.get("gameId").getAsString(), this.getMenuActivity());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setClass( menuActivity, GameActivity.class);
            menuActivity.startActivity(intent);
        }else if(messageStatus.equalsIgnoreCase(ResponseState.GAME_STATE.getState())){
           GameState gameState = gson.fromJson(message, GameState.class);
            gameActivity.setGameState(gameState);
        }else if(messageStatus.equalsIgnoreCase(ResponseState.USER_STATS.getState())){
            UserStats userStats = gson.fromJson(message, UserStats.class);
            LocalCache.getInstance().saveString(USER_ID, userStats.getUserDto().getUserId(), this.getMenuActivity());
            LocalCache.getInstance().save(userStats.getUserDto(), USER_DETAILS, menuActivity);
            menuActivity.getHomeFragment().populatePlayerDetails(userStats.getUserDto());
        }
        else if(messageStatus.equalsIgnoreCase(ResponseState.FRIEND_LIST.getState())){
            FriendList friendList = gson.fromJson(message,FriendList.class);
            getMenuActivity().populateFriendListDialog(friendList);
        }
        else if(messageStatus.equalsIgnoreCase(ResponseState.RANK_LIST.getState())){
            RankList rankList = gson.fromJson(message, RankList.class);
            rankList.setTimestamp(new Date());
            rankList = LocalCache.getInstance().save(rankList, RANK_LIST, menuActivity);
            menuActivity.getRankFragment().setRankList(rankList);
        }
    }

    public Completable findGame(FindGame findGame){
        findGame.setGameId(LocalCache.getInstance().getString(CURRENT_GAME_ID, this.getMenuActivity()));
        addTopic();
       return Client.sendAndSubscribe(Const.FIND_GAME, gson.toJson(findGame));
    }

    public void getFriendList() {
        addTopic();
        Client.send(Const.GET_FRIEND_LIST);
    }

    public void getPlayerDetails(){
        UserDto playerDto = LocalCache.getInstance().get(USER_DETAILS, menuActivity);
        if(playerDto != null){
            menuActivity.getHomeFragment().populatePlayerDetails(playerDto);
        }
        else{
            addTopic();
            Client.getInstance().send(Const.GET_USER_DETAILS);
        }
    }

    public void getPlayerDetailsIfExpired(){
        UserDto playerDto = LocalCache.getInstance().get(USER_DETAILS, menuActivity);
        if(playerDto == null){
            addTopic();
            Client.getInstance().send(Const.GET_USER_DETAILS);
        }
    }

    public void sendReward(Reward reward){
        addTopic();
        Client.send(Const.SEND_REWARD, gson.toJson(reward));
    }

    public void getRankList(){
        RankList rankList = LocalCache.getInstance().get(RANK_LIST, menuActivity);
        if(rankList != null){
            menuActivity.getRankFragment().setRankList(rankList);
        }
        else{
            addTopic();
            GetRank get = new GetRank();
            Client.getInstance().send(Const.GET_RANK, gson.toJson(get));
        }
    }

    public void getRankListIfExpired(){
        RankList rankList = LocalCache.getInstance().get(RANK_LIST, menuActivity);
        if(rankList == null){
            addTopic();
            GetRank get = new GetRank();
            Client.getInstance().send(Const.GET_RANK, gson.toJson(get));
        }
    }
}
