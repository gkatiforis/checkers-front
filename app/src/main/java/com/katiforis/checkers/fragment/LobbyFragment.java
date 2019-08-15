//package com.katiforis.checkers.fragment;
//
//import android.os.Bundle;
//import com.google.android.material.navigation.NavigationView;
//import androidx.fragment.app.Fragment;
//import androidx.drawerlayout.widget.DrawerLayout;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.katiforis.checkers.DTO.UserDto;
//import com.katiforis.checkers.DTO.response.Lobby;
//import com.katiforis.checkers.R;
//import com.katiforis.checkers.adapter.InviteFriendAdapter;
//import com.katiforis.checkers.adapter.PlayerLobbyAdapter;
//import com.katiforis.checkers.controller.LobbyController;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class LobbyFragment extends Fragment {
//    public static LobbyFragment INSTANCE;
//    public static GoogleSignInAccount account;
//
//    private Button startGame;
//    private Button invitePlayer;
//    private Button playWithFriend;
//    private TextView username;
//
//    private RecyclerView invitationFriendRecyclerView;
//    private InviteFriendAdapter inviteFriendAdapter;
//    private List<UserDto> inviteFriends = new ArrayList<>();
//
//    private RecyclerView lobbyRecyclerView;
//    private PlayerLobbyAdapter playerLobbyAdapter;
//    private List<UserDto> lobbyPlayers = new ArrayList<>();
//
//    private LobbyController lobbyController;
//
//    public static LobbyFragment getInstance() {
//        if (INSTANCE == null) {
//            synchronized(LobbyFragment.class) {
//                INSTANCE = new LobbyFragment();
//            }
//        }
//        INSTANCE.lobbyController = LobbyController.getInstance();
//        INSTANCE.lobbyController.setLobbyFragment(INSTANCE);
//        return INSTANCE;
//    }
//
//    public LobbyFragment() {}
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_lobby_layout,  null);
//        DrawerLayout drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
//        startGame = view.findViewById(R.id.start_game);
//        invitePlayer  = view.findViewById(R.id.invite_friend);
//
//        invitePlayer.setOnClickListener(p -> {
//            NavigationView navigationView = getActivity().findViewById(R.id.navigation_view);
//            navigationView.removeAllViews();
//
//            View v = LayoutInflater.from(getActivity())
//                    .inflate(R.layout.fragment_invite_friend_layout, null, false);
//            navigationView.addView(v);
//
//            invitationFriendRecyclerView = (RecyclerView) drawerLayout.findViewById(R.id.invitation_friend_list);
//            invitationFriendRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//            inviteFriendAdapter = new InviteFriendAdapter(inviteFriends);
//            invitationFriendRecyclerView.setAdapter(inviteFriendAdapter);
//
//            drawerLayout.openDrawer(Gravity.RIGHT);
//        });
//
//        startGame.setOnClickListener(p -> {
//
//        });
//
//
//        lobbyRecyclerView = (RecyclerView)view.findViewById(R.id.player_lobby);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getActivity(), 3);
//        lobbyRecyclerView.setLayoutManager(gridLayoutManager);
//         playerLobbyAdapter = new PlayerLobbyAdapter(lobbyPlayers);
//        lobbyRecyclerView.setAdapter(playerLobbyAdapter);
//
//
//        return view;
//    }
//
//    public void getLobby(){
//        lobbyController.getLobby();
//    }
//
//    public void populateLobby(Lobby lobby){
//        getActivity().runOnUiThread(() -> {
//            lobbyPlayers.clear();
//            lobbyPlayers.addAll(lobby.getPlayers());
//            playerLobbyAdapter.notifyDataSetChanged();
//        });
//    }
//}