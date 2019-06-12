package com.katiforis.checkers.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.katiforis.checkers.DTO.UserDto;
import com.katiforis.checkers.DTO.response.GameStats;
import com.katiforis.checkers.R;
import com.katiforis.checkers.adapter.PlayersStatsAdapter;
import com.katiforis.checkers.controller.GameController;

import java.util.ArrayList;
import java.util.List;

public class GameStatsFragment extends DialogFragment {
    private static GameStatsFragment INSTANCE = null;
   GameController gameController;

    public static boolean populated;

    private RecyclerView playersStatsRecyclerView;
    private PlayersStatsAdapter playersStatsAdapter;
    private List<UserDto> players = new ArrayList<>();
    private Button returnToMenu;

    public static GameStatsFragment getInstance() {
        if (INSTANCE == null) {
            synchronized(GameStatsFragment.class) {
                INSTANCE = new GameStatsFragment();
            }
        }
        INSTANCE.gameController = GameController.getInstance();
        INSTANCE.gameController.setGameStatsFragment(INSTANCE);
        return INSTANCE;
    }

    public GameStatsFragment() {
        populated = false;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_game_stats_layout,  null);
        playersStatsRecyclerView = (RecyclerView) view.findViewById(R.id.player_stats_list);
        playersStatsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        playersStatsRecyclerView.addItemDecoration(new DividerItemDecoration(playersStatsRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        playersStatsAdapter = new PlayersStatsAdapter(players);
        returnToMenu = (Button) view.findViewById(R.id.returnToMenu);

        returnToMenu.setOnClickListener(c ->{
           this.getActivity().finish();
        });

        playersStatsRecyclerView.smoothScrollToPosition(0);
        playersStatsAdapter.notifyDataSetChanged();
        playersStatsRecyclerView.setAdapter(playersStatsAdapter);
        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public void setGameStats(GameStats gameStats){
        this.players = gameStats.getPlayers();
        Activity activity = getActivity();
        if(activity != null){
            activity.runOnUiThread(() -> {

                playersStatsAdapter.notifyDataSetChanged();
            });
        }
    }
}