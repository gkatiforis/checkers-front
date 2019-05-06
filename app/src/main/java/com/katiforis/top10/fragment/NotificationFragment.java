package com.katiforis.top10.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.katiforis.top10.DTO.Notification;
import com.katiforis.top10.R;
import com.katiforis.top10.adapter.NotificationAdapter;
import com.katiforis.top10.controller.NotificationController;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends DialogFragment {
    private static NotificationFragment INSTANCE = null;
    NotificationController notificationController;

    public static boolean populated;

    private RecyclerView notificationsRecyclerView;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notifications = new ArrayList<>();
    private Button btnClose;

    public static NotificationFragment getInstance() {
        if (INSTANCE == null) {
            synchronized(NotificationFragment.class) {
                INSTANCE = new NotificationFragment();
            }
        }
        INSTANCE.notificationController = NotificationController.getInstance();
        INSTANCE.notificationController.setNotificationFragment(INSTANCE);
        return INSTANCE;
    }

    public NotificationFragment() {
        populated = false;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_notification_layout,  null);
        notificationsRecyclerView = (RecyclerView) view.findViewById(R.id.notification_list);
        notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        notificationsRecyclerView.addItemDecoration(new DividerItemDecoration(notificationsRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        notificationAdapter = new NotificationAdapter(notifications);


        btnClose = (Button) view.findViewById(R.id.btnClose);

        btnClose.setOnClickListener(c ->{

        });

        notificationsRecyclerView.smoothScrollToPosition(0);
        notificationAdapter.notifyDataSetChanged();
        notificationsRecyclerView.setAdapter(notificationAdapter);
        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
        return dialog;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
        if(!populated){
            notificationController.getNotificationList();
            populated = true;
        }
    }

    public void appendNotifications(List<Notification> notifications){
        Activity activity = getActivity();
        if(activity != null){
            activity.runOnUiThread(() -> {
                this.notifications.addAll(0, notifications);
                notificationAdapter.notifyDataSetChanged();
            });
        }
    }
}