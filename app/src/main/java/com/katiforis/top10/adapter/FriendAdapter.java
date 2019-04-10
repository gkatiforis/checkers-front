package com.katiforis.top10.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.katiforis.top10.DTO.Player;
import com.katiforis.top10.R;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private List<Player> players;

    public class FriendViewHolder extends RecyclerView.ViewHolder {
        public TextView description, username, points;

        public FriendViewHolder(View view) {
            super(view);
            description = (TextView) view.findViewById(R.id.description);
            username = (TextView) view.findViewById(R.id.username);
            points = (TextView) view.findViewById(R.id.points);

        }
    }

    public FriendAdapter(List<Player> players) {
        this.players = players;
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_answer_layout, parent, false);

        return new FriendViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {

        Player player = players.get(position);
        holder.username.setText(player.getUsername());
    }

    @Override
    public int getItemCount() {
        return players.size();
    }
}