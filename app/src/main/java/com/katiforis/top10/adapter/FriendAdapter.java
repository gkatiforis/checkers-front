package com.katiforis.top10.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.katiforis.top10.DTO.UserDto;
import com.katiforis.top10.R;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private List<UserDto> players;

    public class FriendViewHolder extends RecyclerView.ViewHolder {
        public TextView fullName, username, points;

        public FriendViewHolder(View view) {
            super(view);
            fullName = (TextView) view.findViewById(R.id.fullName);
            username = (TextView) view.findViewById(R.id.username);
            points = (TextView) view.findViewById(R.id.points);

        }
    }

    public FriendAdapter(List<UserDto> players) {
        this.players = players;
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend_layout, parent, false);

        return new FriendViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {

        UserDto player = players.get(position);
    //    holder.fullName.setText(player.getFullName());
        holder.username.setText(player.getUsername());
      //  holder.points.setText(player.getPoints().toString());
    }

    @Override
    public int getItemCount() {
        return players.size();
    }
}