package com.katiforis.top10.controller;

import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.katiforis.top10.DTO.PlayerAnswer;
import com.katiforis.top10.DTO.response.ResponseState;
import com.katiforis.top10.activities.GameActivity;
import com.katiforis.top10.activities.MenuActivity;
import com.katiforis.top10.conf.Const;
import com.katiforis.top10.stomp.Client;

import ua.naiksoftware.stomp.dto.StompMessage;


public class GameController extends AbstractController{

    private static GameController INSTANCE = null;

    private GameActivity gameActivity;

    private GameController(){ }

    public static GameController getInstance() {
        if (INSTANCE == null) {
            synchronized (NotificationController.class) {
                INSTANCE = new GameController();
            }
        }
        return INSTANCE;
    }

    public void onReceive(StompMessage stompMessage){
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
                gameActivity.showAnswer(playerAnswer);
            }
    }

    public void sendAnswer(String gameId, Object object ){
        addTopic(GameActivity.getGameId());
        Gson gson = new Gson();
        String jsonInString = gson.toJson(object);
        Client.getInstance().send( Const.SEND_WORD.replace("placeholder", gameId), jsonInString);
    }

    public void getGameState(String userId, String gameId){
        addTopic(GameActivity.getGameId());
        Client.getInstance().send(Const.GET_GAME_STATE, userId + "|" + gameId);
    }

    public GameActivity getGameActivity() {
        return gameActivity;
    }

    public void setGameActivity(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
    }

    public void addTopic(String gameId) {
        if(gameId == null)return;
        super.addTopic(Const.GAME_RESPONSE.replace("placeholder", gameId));
    }
}
