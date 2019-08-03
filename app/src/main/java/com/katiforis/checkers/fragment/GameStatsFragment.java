package com.katiforis.checkers.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.katiforis.checkers.DTO.UserDto;
import com.katiforis.checkers.DTO.response.GameStats;
import com.katiforis.checkers.R;
import com.katiforis.checkers.activities.GameActivity;
import com.katiforis.checkers.activities.MenuActivity;
import com.katiforis.checkers.adapter.PlayersStatsAdapter;
import com.katiforis.checkers.controller.GameController;
import com.katiforis.checkers.util.LocalCache;

import java.util.ArrayList;
import java.util.List;

import info.hoang8f.widget.FButton;

import static com.katiforis.checkers.util.CachedObjectProperties.USER_ID;

public class GameStatsFragment extends DialogFragment {
    private GameActivity gameActivity;
    private static GameStatsFragment INSTANCE = null;
    private GameController gameController;
    private TextView title;
    public static boolean populated;

    private RecyclerView playersStatsRecyclerView;
    private PlayersStatsAdapter playersStatsAdapter;
    private List<UserDto> players = new ArrayList<>();
    private GameStats gameStats;
    private FButton restart;
    private FButton returnToMenu;
    private FButton newOpponent;
    private CountDownTimer countDownTimer;
    private boolean restartGame = false;

    public static GameStatsFragment getInstance() {
            synchronized(GameStatsFragment.class) {
                INSTANCE = new GameStatsFragment();
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
        gameActivity = (GameActivity) getActivity();
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_game_stats_layout,  null);
        playersStatsRecyclerView = (RecyclerView) view.findViewById(R.id.player_stats_list);
        playersStatsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        playersStatsRecyclerView.addItemDecoration(new DividerItemDecoration(playersStatsRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        playersStatsAdapter = new PlayersStatsAdapter(players);
        returnToMenu = (FButton) view.findViewById(R.id.returnToMenu);
        restart = (FButton) view.findViewById(R.id.restartGame);
        newOpponent = (FButton) view.findViewById(R.id.newOpponent);
        title = (TextView) view.findViewById(R.id.statsTitle);

        restart.setButtonColor(getResources().getColor(R.color.fbutton_color_silver2));
        newOpponent.setButtonColor(getResources().getColor(R.color.fbutton_color_silver2));
        returnToMenu.setButtonColor(getResources().getColor(R.color.fbutton_color_silver2));

        returnToMenu.setOnClickListener(c ->{
            gameActivity.getAudioPlayer().playClickButton();
            intentToMenuActivity();
        });

        restart.setOnClickListener(c ->{
            gameActivity.getAudioPlayer().playClickButton();
            gameController.restartGame();
            restartGame = true;
        });

        newOpponent.setOnClickListener(c ->{
            gameActivity.getAudioPlayer().playClickButton();
            restart.setVisibility(View.GONE);
            gameController.findNewOpponent();
        });

        playersStatsRecyclerView.smoothScrollToPosition(0);
        playersStatsAdapter.notifyDataSetChanged();
        playersStatsRecyclerView.setAdapter(playersStatsAdapter);
        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
        dialog.setCanceledOnTouchOutside(false);
        Activity activity = getActivity();
        if(activity != null){
            activity.runOnUiThread(() -> {
                if(gameStats.isDraw()){
                    this.title.setText("Draw");
                }else{
                    if( players.get(0).getUserId().equals(LocalCache.getInstance().getString(USER_ID)) &&
                        players.get(0).getColor().equals(gameStats.getWinnerColor())){
                        this.title.setText("You Win");
                    }else if( players.get(1).getUserId().equals(LocalCache.getInstance().getString(USER_ID)) &&
                            players.get(1).getColor().equals(gameStats.getWinnerColor())){
                        this.title.setText("You Win");
                    }else{
                        this.title.setText("You Lose");
                    }

                }
            });
        }

        if(countDownTimer != null){
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(30 * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                Long remainingSeconds = millisUntilFinished / 1000;
                if(restartGame){
                    restart.setText("Waiting for your opponent (" + remainingSeconds + ")");
                }else {
                    restart.setText("Play again(" + remainingSeconds + ")");
                }
            }
            public void onFinish() {
                restartGame = false;
                restart.setVisibility(View.GONE);
            }
        }.start();

        return dialog;
    }

    private void intentToMenuActivity(){
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setClass(this.getActivity(), MenuActivity.class);
        startActivity(intent);
    }


    public GameStats getGameStats() {
        return gameStats;
    }

    public void setGameStats(GameStats gameStats) {
        this.gameStats = gameStats;
    }

    public void showPlayerList(){
        this.players = gameStats.getPlayers();

        Activity activity = getActivity();
        if(activity != null){
            activity.runOnUiThread(() -> {
                playersStatsAdapter.notifyDataSetChanged();
            });
        }
    }
}