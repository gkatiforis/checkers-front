package com.katiforis.top10.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.katiforis.top10.DTO.PlayerDto;
import com.katiforis.top10.R;

import java.util.ArrayList;
import java.util.List;

public class InvitedFriendAdapter extends RecyclerView.Adapter<InvitedFriendAdapter.InviteFriendViewHolder> {
    List<Integer> selectedPositions = new ArrayList<>();
    private List<PlayerDto> players;

    public class InviteFriendViewHolder extends RecyclerView.ViewHolder {
        public TextView username, points;

        public InviteFriendViewHolder(View view) {
            super(view);
            username = (TextView) view.findViewById(R.id.username);
            points = (TextView) view.findViewById(R.id.points);

            itemView.setOnClickListener(c ->{
                if (getAdapterPosition() == RecyclerView.NO_POSITION) return;

               int selectedPos = getAdapterPosition();

               if(selectedPositions.contains(selectedPos)){
                   selectedPositions.remove(new Integer(selectedPos));
               }else{
                   selectedPositions.add(selectedPos);
               }
                notifyItemChanged(selectedPos);
            });
        }
    }

    public InvitedFriendAdapter(List<PlayerDto> players) {
        this.players = players;
    }

    @Override
    public InviteFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_game_invited_friend_layout, parent, false);

        return new InviteFriendViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InviteFriendViewHolder holder, int position) {
        PlayerDto player = players.get(position);
        holder.username.setText(player.getUsername());
        holder.points.setText(String.valueOf(player.getPlayerDetails().getElo()));
    }

    @Override
    public int getItemCount() {
        return players.size();
    }
}