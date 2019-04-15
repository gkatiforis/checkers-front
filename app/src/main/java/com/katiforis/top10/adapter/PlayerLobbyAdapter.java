package com.katiforis.top10.adapter;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.katiforis.top10.DTO.Player;
import com.katiforis.top10.R;

import java.util.List;

public class PlayerLobbyAdapter extends RecyclerView.Adapter<PlayerLobbyAdapter.PlayerLobbyViewItemHolder> {

    private List<Player> players;

    public PlayerLobbyAdapter(List<Player> players) {
        this.players = players;
    }

    @Override
    public PlayerLobbyViewItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_player_lobby_layout, parent, false);

        final TextView title = (TextView)view.findViewById(R.id.card_view_image_title);
        final ImageView image = (ImageView)view.findViewById(R.id.card_view_image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(image, title.getText().toString(), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

        PlayerLobbyViewItemHolder ret = new PlayerLobbyViewItemHolder(view);
        return ret;
    }

    @Override
    public void onBindViewHolder(PlayerLobbyViewItemHolder holder, int position) {
        if(players!=null) {
            Player player = players.get(position);

            if(player != null) {
                holder.getTitleView().setText("empty slot");
                holder.getImageView().setImageResource(R.drawable.user);
            }
        }
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if(players!=null)
        {
            ret = players.size();
        }
        return ret;
    }


     class PlayerLobbyViewItemHolder extends RecyclerView.ViewHolder {

        private TextView titleView = null;

        private ImageView imageView = null;

        public PlayerLobbyViewItemHolder(View itemView) {
            super(itemView);

            if(itemView != null)
            {
                titleView = (TextView)itemView.findViewById(R.id.card_view_image_title);
                imageView = (ImageView)itemView.findViewById(R.id.card_view_image);
            }
        }

        public TextView getTitleView() {
            return titleView;
        }

        public ImageView getImageView() {
            return imageView;
        }
    }
}