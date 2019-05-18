package com.katiforis.top10.controller;

import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.katiforis.top10.DTO.request.GetRank;
import com.katiforis.top10.DTO.response.RankList;
import com.katiforis.top10.DTO.response.ResponseState;
import com.katiforis.top10.conf.Const;
import com.katiforis.top10.fragment.RankFragment;
import com.katiforis.top10.stomp.Client;
import com.katiforis.top10.util.LocalCache;

import ua.naiksoftware.stomp.dto.StompMessage;

import static com.katiforis.top10.util.CachedObjectProperties.RANK_LIST;


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
            rankList = LocalCache.getInstance().save(rankList, RANK_LIST, rankFragment.getActivity());
            rankFragment.setRankList(rankList);
        }
    }

    public void getRankList(String playerId){
        if(playerId == null)
            return;

        RankList rankList = LocalCache.getInstance().get(RANK_LIST, rankFragment.getActivity());
        if(rankList != null){
            rankFragment.setRankList(rankList);
        }
        else{
            addTopic(playerId);
            GetRank get = new GetRank(playerId);
            Client.getInstance().send(Const.GET_RANK, gson.toJson(get));
        }
    }

    public void getRankListIfExpired(String playerId){
        if(playerId == null)
            return;
        RankList rankList = LocalCache.getInstance().get(RANK_LIST, rankFragment.getActivity());
        if(rankList == null){
            addTopic(playerId);
            GetRank get = new GetRank(playerId);
            Client.getInstance().send(Const.GET_RANK, gson.toJson(get));
        }
    }
}
