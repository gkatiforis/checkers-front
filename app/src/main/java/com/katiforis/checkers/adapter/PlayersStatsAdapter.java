package com.katiforis.checkers.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.katiforis.checkers.DTO.UserDto;
import com.katiforis.checkers.R;
import com.katiforis.checkers.activities.GameActivity;
import com.katiforis.checkers.util.CircleTransform;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlayersStatsAdapter extends RecyclerView.Adapter<PlayersStatsAdapter.PlayersStatsViewHolder> {
    private Context context;
    private List<UserDto> players;

    public class PlayersStatsViewHolder extends RecyclerView.ViewHolder {
        public TextView username, points, pointsExtra, coins, coinsExtra;
        ImageView userImage;

        public PlayersStatsViewHolder(View view) {
            super(view);
            username = (TextView) view.findViewById(R.id.usernameStats);
            points = (TextView) view.findViewById(R.id.pointsStats);
            pointsExtra = (TextView) view.findViewById(R.id.pointsExtra);
            userImage = (ImageView) view.findViewById(R.id.userImage);
            coins = (TextView) view.findViewById(R.id.coins);
            coinsExtra = (TextView) view.findViewById(R.id.coinsExtra);
        }
    }

    public PlayersStatsAdapter(List<UserDto> players, Context context) {
        this.players = players;
        this.context = context;
    }

    @Override
    public PlayersStatsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_player_stats_layout, parent, false);

        return new PlayersStatsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlayersStatsViewHolder holder, int position) {
        UserDto playerDto = players.get(position);
        holder.username.setText(playerDto.getUsername());

        int eloExtra = playerDto.getPlayerDetails().getEloExtra();
        int coinsExtra = playerDto.getPlayerDetails().getCoinsExtra();
        int elo = playerDto.getPlayerDetails().getElo();
        int finalElo = elo - eloExtra;
        int coins = playerDto.getPlayerDetails().getCoins();
        int finalCoins = coins - coinsExtra;

        if(elo == 0){
            holder.points.setText("" + 0);
        }else{
            holder.points.setText("" + finalElo);
        }

        if(coins == 0){
            holder.coins.setText("" + 0);
        }else{
            holder.coins.setText("" + finalCoins);
        }


        if(eloExtra < 0){
            holder.pointsExtra.setText("" + eloExtra);
        }else{
            holder.pointsExtra.setText(" +" + eloExtra);
        }
        if(coinsExtra < 0){
            holder.coinsExtra.setText("" + coinsExtra);
        }else{
            holder.coinsExtra.setText(" +" + coinsExtra);
        }

        Picasso.with(context)
                .load(playerDto.getPictureUrl())
                .error(R.mipmap.ic_launcher)
                .placeholder(context.getResources().getDrawable(R.drawable.user))
                .transform(new CircleTransform())
                .into(holder.userImage, new Callback() {
                    @Override
                    public void onSuccess() {     }

                    @Override
                    public void onError() {
                        //TODO
                    }
                });

    }

    @Override
    public int getItemCount() {
        return players.size();
    }
}