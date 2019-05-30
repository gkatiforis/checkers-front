package com.katiforis.top10.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.katiforis.top10.DTO.UserDto;
import com.katiforis.top10.R;
import com.katiforis.top10.activities.MenuActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.RankViewHolder> {

    private List<UserDto> rankList;

    public class RankViewHolder extends RecyclerView.ViewHolder {
        public TextView rank, username, level, points;
        ImageView playerImage;

        public RankViewHolder(View view) {
            super(view);
            playerImage = (ImageView) view.findViewById(R.id.playerImage);
            username = (TextView) view.findViewById(R.id.username);
            rank = (TextView) view.findViewById(R.id.rank);
            level = (TextView) view.findViewById(R.id.level);
            points = (TextView) view.findViewById(R.id.points);
        }
    }

    public RankAdapter(List<UserDto> rankList) {
        this.rankList = rankList;
    }

    @Override
    public RankViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rank_player_layout, parent, false);
        return new RankViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RankViewHolder holder, int position) {

        UserDto player = rankList.get(position);

            Picasso.with(MenuActivity.getAppContext())
                    .load("https://www.uni-regensburg.de/Fakultaeten/phil_Fak_II/Psychologie/Psy_II/beautycheck/english/durchschnittsgesichter/m(01-32)_gr.jpg")
                    .error(R.mipmap.ic_launcher)
                    .into(holder.playerImage, new Callback() {
                        @Override
                        public void onSuccess() {     }

                        @Override
                        public void onError() {
                            //TODO
                        }
                    });
            holder.username.setText(player.getUsername());
    }


    @Override
    public int getItemCount() {
        return rankList.size();
    }
}