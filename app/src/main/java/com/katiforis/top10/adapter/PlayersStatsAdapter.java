package com.katiforis.top10.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.katiforis.top10.DTO.PlayerDto;
import com.katiforis.top10.R;

import java.util.List;

public class PlayersStatsAdapter extends RecyclerView.Adapter<PlayersStatsAdapter.PlayersStatsViewHolder> {

    private List<PlayerDto> players;

    public class PlayersStatsViewHolder extends RecyclerView.ViewHolder {
        public TextView username, points, pointsExtra;
        ImageView userImage;

        public PlayersStatsViewHolder(View view) {
            super(view);
            username = (TextView) view.findViewById(R.id.usernameStats);
            points = (TextView) view.findViewById(R.id.pointsStats);
            pointsExtra = (TextView) view.findViewById(R.id.pointsExtra);
            userImage = (ImageView) view.findViewById(R.id.userImage);
        }
    }

    public PlayersStatsAdapter(List<PlayerDto> players) {
        this.players = players;
    }

    @Override
    public PlayersStatsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_player_stats_layout, parent, false);

        return new PlayersStatsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlayersStatsViewHolder holder, int position) {
        PlayerDto playerDto = players.get(position);
        holder.username.setText(playerDto.getUsername());
        holder.points.setText(String.valueOf(playerDto.getPlayerDetails().getElo()));
        holder.pointsExtra.setText(" + " + String.valueOf(playerDto.getPlayerDetails().getEloExtra()));
    }

    @Override
    public int getItemCount() {
        return players.size();
    }
}