package com.katiforis.checkers.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.katiforis.checkers.DTO.LevelEnum;
import com.katiforis.checkers.DTO.UserDto;
import com.katiforis.checkers.R;
import com.katiforis.checkers.util.CircleTransform;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.katiforis.checkers.DTO.LevelEnum.LEVEL_1;
import static com.katiforis.checkers.DTO.LevelEnum.LEVEL_2;
import static com.katiforis.checkers.DTO.LevelEnum.LEVEL_3;
import static com.katiforis.checkers.DTO.LevelEnum.LEVEL_4;
import static com.katiforis.checkers.DTO.LevelEnum.LEVEL_5;
import static com.katiforis.checkers.DTO.LevelEnum.LEVEL_6;
import static com.katiforis.checkers.DTO.LevelEnum.mapPointsToLevel;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.RankViewHolder> {

    private List<UserDto> rankList;
    private Context context;

    public class RankViewHolder extends RecyclerView.ViewHolder {
        public TextView rank, username, level, points;
        ImageView playerImage, playerLvlImage;

        public RankViewHolder(View view) {
            super(view);
            playerImage = (ImageView) view.findViewById(R.id.playerImage);
            playerLvlImage = (ImageView) view.findViewById(R.id.playerLvlImage);
            username = (TextView) view.findViewById(R.id.username);
            rank = (TextView) view.findViewById(R.id.rank);
            level = (TextView) view.findViewById(R.id.level);
            points = (TextView) view.findViewById(R.id.points);
        }
    }

    public RankAdapter(Context context, List<UserDto> rankList) {
        this.rankList = rankList;
        this.context = context;
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

        Picasso.with(this.context)
                .load(player.getPictureUrl())
                .placeholder(this.context.getResources().getDrawable(R.drawable.user))
                .error(R.mipmap.ic_launcher)
                .transform(new CircleTransform())
                .into(holder.playerImage, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        //TODO
                    }
                });
        holder.username.setText(player.getUsername());
        holder.rank.setText(position + 1 + ".");
        holder.level.setText("level " + String.valueOf(player.getPlayerDetails().getLevel()));


        int elo = player.getPlayerDetails().getElo();
        holder.points.setText(String.valueOf(elo));

        LevelEnum level = mapPointsToLevel(elo);
        if (level == LEVEL_1)
            holder.playerLvlImage.setImageResource(R.drawable.level1);
        else if (level == LEVEL_2)
            holder.playerLvlImage.setImageResource(R.drawable.level2);
        else if (level == LEVEL_3)
            holder.playerLvlImage.setImageResource(R.drawable.level3);
        else if (level == LEVEL_4)
            holder.playerLvlImage.setImageResource(R.drawable.level4);
        else if (level == LEVEL_5)
            holder.playerLvlImage.setImageResource(R.drawable.level5);
        else if (level == LEVEL_6)
            holder.playerLvlImage.setImageResource(R.drawable.level6);
    }

    @Override
    public int getItemCount() {
        return rankList.size();
    }
}