package com.katiforis.top10.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.katiforis.top10.DTO.Player;
import com.katiforis.top10.R;

import java.util.ArrayList;
import java.util.List;

public class InviteFriendFragment extends DialogFragment {
    private RecyclerView invitationFriendRecyclerView;
    private com.katiforis.top10.adapter.InviteFriendAdapter inviteFriendAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Player> friends = new ArrayList<>();

    public static InviteFriendFragment newInstance(int title) {
        InviteFriendFragment frag = new InviteFriendFragment();
//        Bundle args = new Bundle();
//        args.putInt("title", title);
//        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_game_invite_friend_layout,  null);
        invitationFriendRecyclerView = (RecyclerView) view.findViewById(R.id.invitation_friend_list);
        invitationFriendRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        inviteFriendAdapter = new com.katiforis.top10.adapter.InviteFriendAdapter(friends);

        Player player = new Player("George");
        friends.add(player);
        friends.add(player);
        friends.add(player);
        friends.add(player);
        friends.add(player);
        friends.add(player);
        friends.add(player);
        friends.add(player);
        friends.add(player);
        friends.add(player);
        friends.add(player);
        friends.add(player);
        friends.add(player);
        friends.add(player);
        friends.add(player);
        friends.add(player);
        friends.add(player);
        friends.add(player);
        friends.add(player);
        friends.add(player);
        friends.add(player);
        invitationFriendRecyclerView.smoothScrollToPosition(0);
        inviteFriendAdapter.notifyDataSetChanged();
        invitationFriendRecyclerView.setAdapter(inviteFriendAdapter);
        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
        return dialog;
    }
}