package com.katiforis.top10.fragment;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.katiforis.top10.DTO.Player;
import com.katiforis.top10.R;
import com.katiforis.top10.adapter.InviteFriendAdapter;
import com.katiforis.top10.adapter.PlayerLobbyAdapter;

import java.util.ArrayList;
import java.util.List;

public class LobbyFragment extends Fragment {
    public static LobbyFragment instance;
    public static GoogleSignInAccount account;

    private Button startGame;
    private Button invitePlayer;
    private Button playWithFriend;
    private TextView username;

    private RecyclerView invitationFriendRecyclerView;
    private InviteFriendAdapter inviteFriendAdapter;
    private List<Player> inviteFriends = new ArrayList<>();


    public LobbyFragment() { instance = this;}


    public static LobbyFragment newInstance(int title) {

        LobbyFragment frag = new LobbyFragment();
//        Bundle args = new Bundle();
//        args.putInt("title", title);
//        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_lobby_layout,  null);
        DrawerLayout drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.activity_mainn);
        startGame = v.findViewById(R.id.start_game);
        invitePlayer  = v.findViewById(R.id.invite_friend);

        invitePlayer.setOnClickListener(p -> {
            NavigationView navigationView = getActivity().findViewById(R.id.nv);
            navigationView.removeAllViews();

            View view = LayoutInflater.from(getActivity())
                    .inflate(R.layout.fragment_invite_friend_layout, null, false);
            navigationView.addView(view);

            invitationFriendRecyclerView = (RecyclerView) drawerLayout.findViewById(R.id.invitation_friend_list);
            invitationFriendRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            inviteFriendAdapter = new InviteFriendAdapter(inviteFriends);

            Player player2 = new Player(1l, "id","gkatiforis", "George Katiforis", 100, 1);
            inviteFriends.add(player2);
            inviteFriends.add(player2);
            inviteFriends.add(player2);


            invitationFriendRecyclerView.smoothScrollToPosition(0);
            inviteFriendAdapter.notifyDataSetChanged();
            invitationFriendRecyclerView.setAdapter(inviteFriendAdapter);


            drawerLayout.openDrawer(Gravity.RIGHT);
        });

        startGame.setOnClickListener(p -> {

        });


        RecyclerView carRecyclerView = (RecyclerView)v.findViewById(R.id.player_lobby);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getActivity(), 3);
        carRecyclerView.setLayoutManager(gridLayoutManager);
        Player player2 = new Player(1l, "id","gkatiforis", "George Katiforis", 100, 1);
        inviteFriends.add(player2);
        inviteFriends.add(player2);
        inviteFriends.add(player2);
        inviteFriends.add(player2);
        inviteFriends.add(player2);
        inviteFriends.add(player2);
        inviteFriends.add(player2);
        inviteFriends.add(player2);
        inviteFriends.add(player2);
        PlayerLobbyAdapter playerLobbyAdapter = new PlayerLobbyAdapter(inviteFriends);
        carRecyclerView.setAdapter(playerLobbyAdapter);
        return v;
    }
}