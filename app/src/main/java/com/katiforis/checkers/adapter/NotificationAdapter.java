package com.katiforis.checkers.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.katiforis.checkers.DTO.Notification;
import com.katiforis.checkers.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> notifications;

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        public TextView description, date;
        TextView text;

        public NotificationViewHolder(View view) {
            super(view);
            description = (TextView) view.findViewById(R.id.description);
            date = (TextView) view.findViewById(R.id.date);

        }
    }

    public NotificationAdapter(List<Notification> notifications) {
        this.notifications = notifications;
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification_layout, parent, false);

        return new NotificationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {

        Notification notification = notifications.get(position);
        holder.description.setText(notification.getMessage());
        holder.date.setText(notification.getDate());
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }
}