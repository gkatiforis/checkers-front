package com.katiforis.checkers.adapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.katiforis.checkers.DTO.PlayerAnswer;
import com.katiforis.checkers.R;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder> {

    private List<PlayerAnswer> playerAnswers;

    public class AnswerViewHolder extends RecyclerView.ViewHolder {
        public TextView description, username, points;

        public AnswerViewHolder(View view) {
            super(view);
            description = (TextView) view.findViewById(R.id.description);
            username = (TextView) view.findViewById(R.id.username);
            points = (TextView) view.findViewById(R.id.points);
        }
    }

    public AnswerAdapter(List<PlayerAnswer> playerAnswers) {
        this.playerAnswers = playerAnswers;
    }

    @Override
    public AnswerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_answer_layout, parent, false);

        return new AnswerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AnswerViewHolder holder, int position) {
        PlayerAnswer playerAnswer = playerAnswers.get(position);
        holder.description.setText(playerAnswer.getDescription());
        holder.username.setText(playerAnswer.getPlayer().getUsername());
        holder.points.setText(playerAnswer.getPoints().toString());
    }

    @Override
    public int getItemCount() {
        return playerAnswers.size();
    }
}