package com.katiforis.checkers.controller;

import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.katiforis.checkers.DTO.request.GetRank;
import com.katiforis.checkers.DTO.response.RankList;
import com.katiforis.checkers.DTO.response.ResponseState;
import com.katiforis.checkers.conf.Const;
import com.katiforis.checkers.fragment.RankFragment;
import com.katiforis.checkers.stomp.Client;
import com.katiforis.checkers.util.LocalCache;

import java.util.Date;

import ua.naiksoftware.stomp.dto.StompMessage;

import static com.katiforis.checkers.util.CachedObjectProperties.RANK_LIST;


public class RankController extends MenuController {
    private static RankController INSTANCE = null;

    private RankFragment rankFragment;

    private RankController() {}

    public static RankController getInstance() {
        if (INSTANCE == null) {
            synchronized (RankController.class) {
                INSTANCE = new RankController();
            }
        }
        return INSTANCE;
    }

    public void setRankFragment(RankFragment rankFragment) {
        this.rankFragment = rankFragment;
    }

    @Override
    public void onReceive(StompMessage stompMessage) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jo = (JsonObject) jsonParser.parse(stompMessage.getPayload());

        Log.i(Const.TAG, "Receive: " + jo.toString());

        JsonObject message = jo.getAsJsonObject("body");
        String messageStatus = message.get("status").getAsString();

        Log.i(Const.TAG, "Receive: " + messageStatus);
         if(messageStatus.equalsIgnoreCase(ResponseState.RANK_LIST.getState())){
            RankList rankList = gson.fromJson(message, RankList.class);
            rankList.setTimestamp(new Date());
            rankList = LocalCache.getInstance().save(rankList, RANK_LIST, rankFragment.getActivity());
            rankFragment.setRankList(rankList);
        }
    }

    public void getRankList(){
        RankList rankList = LocalCache.getInstance().get(RANK_LIST, rankFragment.getActivity());
        if(rankList != null){
            rankFragment.setRankList(rankList);
        }
        else{
            addTopic(false);
            GetRank get = new GetRank();
            Client.getInstance().send(Const.GET_RANK, gson.toJson(get));
        }
    }

    public void getRankListIfExpired(){
        RankList rankList = LocalCache.getInstance().get(RANK_LIST, rankFragment.getActivity());
        if(rankList == null){
            addTopic(false);
            GetRank get = new GetRank();
            Client.getInstance().send(Const.GET_RANK, gson.toJson(get));
        }
    }
}
