package com.katiforis.top10.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.katiforis.top10.DTO.Player;
import com.katiforis.top10.R;

import java.util.ArrayList;
import java.util.List;

public class InviteFriendAdapter extends RecyclerView.Adapter<InviteFriendAdapter.InviteFriendViewHolder> {
    List<Integer> selectedPositions = new ArrayList<>();
    private List<Player> players;

    public class InviteFriendViewHolder extends RecyclerView.ViewHolder {
        public TextView description, username, points;

        public InviteFriendViewHolder(View view) {
            super(view);
            description = (TextView) view.findViewById(R.id.description);
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

    public InviteFriendAdapter(List<Player> players) {
        this.players = players;
    }

    @Override
    public InviteFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_answer_layout, parent, false);

        return new InviteFriendViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InviteFriendViewHolder holder, int position) {

        holder.itemView.setBackgroundColor(selectedPositions.contains(position) ? Color.YELLOW : Color.TRANSPARENT);

        Player player = players.get(position);
        holder.username.setText(player.getUsername());
    }

    @Override
    public int getItemCount() {
        return players.size();
    }
}