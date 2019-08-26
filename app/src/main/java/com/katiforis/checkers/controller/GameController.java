package com.katiforis.checkers.controller;

import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.katiforis.checkers.DTO.GameType;
import com.katiforis.checkers.DTO.PlayerAnswer;
import com.katiforis.checkers.DTO.request.FindGame;
import com.katiforis.checkers.DTO.response.GameStats;
import com.katiforis.checkers.DTO.response.OfferDraw;
import com.katiforis.checkers.DTO.response.ResponseState;
import com.katiforis.checkers.activities.GameActivity;
import com.katiforis.checkers.conf.Const;
import com.katiforis.checkers.fragment.GameStatsFragment;
import com.katiforis.checkers.stomp.Client;
import com.katiforis.checkers.util.LocalCache;

import io.reactivex.Completable;
import ua.naiksoftware.stomp.dto.StompMessage;

import static com.katiforis.checkers.util.CachedObjectProperties.CURRENT_GAME_ID;


public class GameController extends AbstractController {

    private static GameController INSTANCE = null;

    private GameActivity gameActivity;

    private GameStatsFragment gameStatsFragment;

    public boolean isConnected = true;

    private Handler sendMoveHandler;
    private Handler gameStateHandler;

    private GameController() {
    }

    public static GameController getInstance() {
        if (INSTANCE == null) {
            synchronized (GameController.class) {
                INSTANCE = new GameController();
            }
        }
        return INSTANCE;
    }

    public void onReceive(StompMessage stompMessage) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jo = (JsonObject) jsonParser.parse(stompMessage.getPayload());

        JsonObject message = jo.getAsJsonObject("body");
        String messageStatus = message.get("status").getAsString();
        //JSONObject message= messageWrapper.getJSONObject("message");

        if (messageStatus.equalsIgnoreCase(ResponseState.END_GAME.getState())) {
            // Client.clearTopics(this.getClass().getName());
//            LocalCache.getInstance().saveString(CURRENT_GAME_ID, null);

            GameStats gameStats = gson.fromJson(message, GameStats.class);
            openGameStatsDialog(gameStats);


            //TODO: save user details in cache and remove loading from backend
//                UserDto userDto = LocalCache.getInstance().get(USER_DETAILS, MenuActivity.INSTANCE);
//
//                for(UserDto user:gameStats.getPlayers()){
//                    if(user.getUserId().equals(userDto.getUserId())){
//                        userDto.getPlayerDetails().setCoins(user.getPlayerDetails().getCoins()+user.getPlayerDetails().getCoinsExtra());
//                        userDto.getPlayerDetails().setElo(user.getPlayerDetails().getElo()+user.getPlayerDetails().getEloExtra());
//                        userDto.getPlayerDetails().setLevelPoints(user.getPlayerDetails().getLevelPoints()+user.getPlayerDetails().getLevelExtra());
//                        userDto.getPlayerDetails().setLevel(user.getPlayerDetails().getLevel());
//                        LocalCache.getInstance().save(userDto, USER_DETAILS, MenuActivity.INSTANCE);
//                    }
//                }
        } else if (messageStatus.equalsIgnoreCase(ResponseState.ANSWER.getState())) {
            PlayerAnswer playerAnswer = gson.fromJson(message, PlayerAnswer.class);
            if (playerAnswer.getMove().isValid()) {
                gameActivity.makeMove(playerAnswer);
            }
        } else if (messageStatus.equalsIgnoreCase(ResponseState.OFFER_DRAW.getState())) {
            OfferDraw offerDraw = gson.fromJson(message, OfferDraw.class);
            gameActivity.showOfferDraw(offerDraw);
        }
    }

    public Completable restartGame() {
        if (!isConnected()) {
            return Completable.never();
        }
        FindGame findGame = new FindGame(LocalCache.getInstance().getString(CURRENT_GAME_ID, gameActivity));
        findGame.setRestart(true);
        findGame.setGameType(GameType.FRIENDLY);
//        addTopic(false);
        return HomeController.getInstance().findGame(findGame);
    }

    public Completable findNewOpponent() {
        if (!isConnected()) {
            return Completable.never();
        }
        FindGame findGame = new FindGame();
        findGame.setGameType(GameType.FRIENDLY);
        return HomeController.getInstance().findGame(findGame);
    }

    public Completable sendAnswer(String gameId, Object object) {
        //addTopic(LocalCache.getInstance().getString(CURRENT_GAME_ID));
        String jsonInString = gson.toJson(object);
        return Client.getInstance().sendAndSubscribe(Const.SEND_WORD.replace("placeholder", gameId), jsonInString);
    }

    public void getGameState() {
        String gameId = LocalCache.getInstance().getString(CURRENT_GAME_ID, this.getGameActivity());
        if (gameId != null || gameId.length() > 0) {
            addTopic(gameId);
            HomeController.getInstance().addTopic();
            Client.getInstance().send(Const.GET_GAME_STATE, gameId);
        }
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
        if (gameId == null) return;
        super.addTopic(Const.GAME_RESPONSE.replace("placeholder", gameId));
    }

    public void openGameStatsDialog(GameStats gameStats) {
        closeGameStatsDialog();
        gameStatsFragment = new GameStatsFragment(this);
        gameStatsFragment.setGameStats(gameStats);
        gameStatsFragment.show(gameActivity.getSupportFragmentManager(), GameStatsFragment.class.getName());
        gameStatsFragment.showPlayerList();
    }

    public void closeGameStatsDialog() {
        if (gameStatsFragment != null) {
            gameStatsFragment.dismiss();
            gameStatsFragment = null;
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
