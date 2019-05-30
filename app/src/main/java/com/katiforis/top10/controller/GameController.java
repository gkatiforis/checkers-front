package com.katiforis.top10.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.katiforis.top10.DTO.PlayerAnswer;
import com.katiforis.top10.DTO.response.GameStats;
import com.katiforis.top10.DTO.response.ResponseState;
import com.katiforis.top10.activities.GameActivity;
import com.katiforis.top10.conf.Const;
import com.katiforis.top10.fragment.GameStatsFragment;
import com.katiforis.top10.stomp.Client;
import com.katiforis.top10.util.LocalCache;

import ua.naiksoftware.stomp.dto.StompMessage;

import static com.katiforis.top10.util.CachedObjectProperties.CURRENT_GAME_ID;


public class GameController extends AbstractController{

    private static GameController INSTANCE = null;

    private GameActivity gameActivity;

    private GameStatsFragment gameStatsFragment;

    private GameController(){ }

    public static GameController getInstance() {
        if (INSTANCE == null) {
            synchronized (GameController.class) {
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
                Client.clearTopics(this.getClass().getName());
                Gson gson = new Gson();
                GameStats gameStats = gson.fromJson(message, GameStats.class);
                gameStatsFragment = GameStatsFragment.getInstance();
                gameStatsFragment.show(gameActivity.getSupportFragmentManager(), "");
                gameStatsFragment.setGameStats(gameStats);
            } else if (messageStatus.equalsIgnoreCase(ResponseState.ANSWER.getState())) {
                Gson gson = new Gson();
                PlayerAnswer playerAnswer = gson.fromJson(message, PlayerAnswer.class);
                gameActivity.showAnswer(playerAnswer);
            }
    }

    public void sendAnswer(String gameId, Object object ){
        addTopic(LocalCache.getInstance().getString(CURRENT_GAME_ID));
        Gson gson = new Gson();
        String jsonInString = gson.toJson(object);
        Client.getInstance().send( Const.SEND_WORD.replace("placeholder", gameId), jsonInString);
    }

    public void getGameState(String gameId){
        addTopic(LocalCache.getInstance().getString(CURRENT_GAME_ID));
        Client.getInstance().send(Const.GET_GAME_STATE,  gameId);
    }

    public GameActivity getGameActivity() {
        return gameActivity;
    }

    public void setGameActivity(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
    }

    public void setGameStatsFragment(GameStatsFragment gameStatsFragment) {
        this.gameStatsFragment = gameStatsFragment;
    }

    public void addTopic(String gameId) {
        if(gameId == null)return;
        super.addTopic(Const.GAME_RESPONSE.replace("placeholder", gameId));
    }
}
