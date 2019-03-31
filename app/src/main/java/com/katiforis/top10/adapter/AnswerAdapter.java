package com.katiforis.top10.adapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.katiforis.top10.R;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.MyViewHolder> {

    private List<AnswerItem> answerItems;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView description, username, points;

        public MyViewHolder(View view) {
            super(view);
            description = (TextView) view.findViewById(R.id.description);
            username = (TextView) view.findViewById(R.id.username);
            points = (TextView) view.findViewById(R.id.points);
        }
    }


    public AnswerAdapter(List<AnswerItem> moviesList) {
        this.answerItems = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.answer_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AnswerItem answerItem = answerItems.get(position);
        holder.description.setText(answerItem.getDescription());
        holder.username.setText(answerItem.getUsername());
        holder.points.setText(answerItem.getPoints());
    }

    @Override
    public int getItemCount() {
        return answerItems.size();
    }
}