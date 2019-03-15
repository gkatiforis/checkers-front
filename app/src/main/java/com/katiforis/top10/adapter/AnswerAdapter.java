package com.katiforis.top10.adapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.katiforis.top10.R;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.MyViewHolder> {

    private List<AnswerItem> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            genre = (TextView) view.findViewById(R.id.username);
            year = (TextView) view.findViewById(R.id.year);
        }
    }


    public AnswerAdapter(List<AnswerItem> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.answer_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AnswerItem answerItem = moviesList.get(position);
        holder.title.setText(answerItem.getTitle());
        holder.genre.setText(answerItem.getGenre());
        holder.year.setText(answerItem.getYear());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}