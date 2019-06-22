package com.katiforis.checkers.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.katiforis.checkers.DTO.UserDto;
import com.katiforis.checkers.R;

import java.util.ArrayList;
import java.util.List;

public class InviteFriendAdapter extends RecyclerView.Adapter<InviteFriendAdapter.InviteFriendViewHolder> {
    List<Integer> selectedPositions = new ArrayList<>();
    private List<UserDto> players;

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

    public InviteFriendAdapter(List<UserDto> players) {
        this.players = players;
    }

    @Override
    public InviteFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_game_invite_friend_layout, parent, false);

        return new InviteFriendViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InviteFriendViewHolder holder, int position) {
        UserDto player = players.get(position);
        holder.username.setText(player.getUsername());
        holder.points.setText(String.valueOf(player.getPlayerDetails().getElo()));
    }

    @Override
    public int getItemCount() {
        return players.size();
    }
}