package com.katiforis.top10.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.katiforis.top10.DTO.Player;
import com.katiforis.top10.R;
import com.katiforis.top10.activities.MenuActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>{
    private List<Player> players;
    Context context;

    public PlayerAdapter(List<Player> players, Context context){
        this.players = players;
        this.context = context;
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_layout, parent, false);
        PlayerViewHolder playerViewHolder = new PlayerViewHolder(view);
        return playerViewHolder;
    }

    @Override
    public void onBindViewHolder(PlayerViewHolder holder, final int position) {
        Picasso.with(MenuActivity.getAppContext())
                .load("https://www.uni-regensburg.de/Fakultaeten/phil_Fak_II/Psychologie/Psy_II/beautycheck/english/durchschnittsgesichter/m(01-32)_gr.jpg")
                .error(R.mipmap.ic_launcher)
                .into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {     }

                    @Override
                    public void onError() {
                        //TODO
                    }
                });

        //holder.imageView.setImageResource(players.get(position).getImageUrl());
        holder.textView.setText(players.get(position).getUsername() + "(" + players.get(position).getPoints() + ")");
//        holder.imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public class PlayerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public PlayerViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.playerImage);
            textView = view.findViewById(R.id.username);
        }
    }
}