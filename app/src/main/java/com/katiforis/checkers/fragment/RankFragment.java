package com.katiforis.checkers.fragment;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.katiforis.checkers.DTO.UserDto;
import com.katiforis.checkers.DTO.response.RankList;
import com.katiforis.checkers.R;
import com.katiforis.checkers.activities.MenuActivity;
import com.katiforis.checkers.adapter.RankAdapter;
import com.katiforis.checkers.controller.RankController;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RankFragment extends Fragment {
    private static RankFragment INSTANCE;
    RankController rankController;

    public static boolean populated;

    private RecyclerView rankRecyclerView;
    private RankAdapter rankAdapter;
    private List<UserDto> rankList = new ArrayList<>();

    private TextView rank, username, level, points;
    private ImageView playerImage;

    private TextView rank2, username2, level2, points2;
    private ImageView playerImage2;

    private TextView rank3, username3, level3, points3;
    private ImageView playerImage3;

    public static RankFragment getInstance() {
        if (INSTANCE == null) {
            synchronized(RankFragment.class) {
                INSTANCE = new RankFragment();
            }
        }
        INSTANCE.rankController = RankController.getInstance();
        INSTANCE.rankController.setRankFragment(INSTANCE);
        return INSTANCE;
    }

    public RankFragment() {
        populated = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_rank_layout,  null);
        rankRecyclerView = (RecyclerView) view.findViewById(R.id.rank_list);
        rankRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        rankRecyclerView.addItemDecoration(new DividerItemDecoration(rankRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        rankAdapter = new RankAdapter(rankList);

        rankRecyclerView.smoothScrollToPosition(0);
        rankAdapter.notifyDataSetChanged();
        rankRecyclerView.setAdapter(rankAdapter);

        playerImage = (ImageView) view.findViewById(R.id.firstImage);
        username = (TextView) view.findViewById(R.id.firstUsername);
        rank = (TextView) view.findViewById(R.id.rank);
        level = (TextView) view.findViewById(R.id.level);
        points = (TextView) view.findViewById(R.id.points);

        playerImage2 = (ImageView) view.findViewById(R.id.secondImage);
        username2 = (TextView) view.findViewById(R.id.secondUsername);
        rank2 = (TextView) view.findViewById(R.id.rank);
        level2 = (TextView) view.findViewById(R.id.level);
        points2 = (TextView) view.findViewById(R.id.points);

        playerImage3 = (ImageView) view.findViewById(R.id.thirdImage);
        username3 = (TextView) view.findViewById(R.id.thirdUsername);
        rank3 = (TextView) view.findViewById(R.id.rank);
        level3 = (TextView) view.findViewById(R.id.level);
        points3 = (TextView) view.findViewById(R.id.points);

       return view;
    }

    public void getRankList(){
        if(!populated){
            rankController.getRankList();
            populated = true;
        }else{
            rankController.getRankListIfExpired();
        }
    }

    public void setRankList(RankList rankList){
        Activity activity = getActivity();
        if(activity != null){
            activity.runOnUiThread(() -> {
               List<UserDto> players = rankList.getPlayers();
               if(players.size() >=3){
                   UserDto first = players.get(0);
                   UserDto second = players.get(1);
                   UserDto third = players.get(2);
                   players.remove(0);
                   players.remove(0);
                   players.remove(0);

                   Picasso.with(MenuActivity.getAppContext())
                           .load(first.getPictureUrl())
                           .error(R.mipmap.ic_launcher)
                           .into(playerImage);
                   username.setText(first.getUsername());

                   Picasso.with(MenuActivity.getAppContext())
                           .load(second.getPictureUrl())
                           .error(R.mipmap.ic_launcher)
                           .into(playerImage2);
                   username2.setText(second.getUsername());

                   Picasso.with(MenuActivity.getAppContext())
                           .load(third.getPictureUrl())
                           .error(R.mipmap.ic_launcher)
                           .into(playerImage3);
                   username3.setText(third.getUsername());
               }

                this.rankList.clear();
                this.rankList.addAll(players);
                rankAdapter.notifyDataSetChanged();
            });
        }
    }
}