package com.katiforis.top10.fragment;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.katiforis.top10.DTO.Notification;
import com.katiforis.top10.R;
import com.katiforis.top10.adapter.NotificationAdapter;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends DialogFragment {
    private RecyclerView notificationsRecyclerView;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notifications = new ArrayList<>();
    private Button btnClose;

    public NotificationFragment() {}


    public static NotificationFragment newInstance(int title) {
        NotificationFragment frag = new NotificationFragment();
//        Bundle args = new Bundle();
//        args.putInt("title", title);
//        frag.setArguments(args);
        return frag;
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
        Notification notification = new Notification("Game Game Game invitation from George Katiforis Katiforis Katiforis Katiforis", "22/02/2019");
        notifications.add(notification);
        notifications.add(notification);
        notifications.add(notification);
        notifications.add(notification);
        notifications.add(notification);

        notificationsRecyclerView.smoothScrollToPosition(0);
        notificationAdapter.notifyDataSetChanged();
        notificationsRecyclerView.setAdapter(notificationAdapter);
        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
        return dialog;
    }


}