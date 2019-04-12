package com.katiforis.top10.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.katiforis.top10.DTO.Player;
import com.katiforis.top10.R;
import com.katiforis.top10.activities.GameActivity;
import com.katiforis.top10.activities.MenuActivity;
import com.katiforis.top10.adapter.InviteFriendAdapter;
import com.katiforis.top10.controller.MenuController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainFragment extends Fragment {

    public static GoogleSignInAccount account;

    private Button play;
    private Button playWithFriend;
    private TextView username;

    private RecyclerView invitationFriendRecyclerView;
    private com.katiforis.top10.adapter.InviteFriendAdapter inviteFriendAdapter;
    private List<Player> inviteFriends = new ArrayList<>();

    public MainFragment() {}


    public static MainFragment newInstance(int title) {
        MainFragment frag = new MainFragment();
//        Bundle args = new Bundle();
//        args.putInt("title", title);
//        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_main_layout,  null);

        play = v.findViewById(R.id.play);
        playWithFriend = v.findViewById(R.id.play_with_friend);
        username = v.findViewById(R.id.username);

        if(MenuActivity.userId == null){
            startSignInIntent();
        }else{
            username.setText(account.getDisplayName());

        }

        play.setOnClickListener(p -> {
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("fromUserID", MenuActivity.userId);
				jsonObject.put("gameId", GameActivity.getGameId());
			} catch (JSONException e) {
				e.printStackTrace();
			}

			MenuController.sendFindGameRequest(jsonObject);
        });

        playWithFriend.setOnClickListener(p -> {
            openPlayWithFriendDialog();
        });

        return v;
    }

    private void openPlayWithFriendDialog(){

        DrawerLayout drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.activity_mainn);
        NavigationView navigationView = getActivity().findViewById(R.id.nv);
        navigationView.removeAllViews();

        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_game_invite_friend_layout, null, false);
        navigationView.addView(view);

        invitationFriendRecyclerView = (RecyclerView) drawerLayout.findViewById(R.id.invitation_friend_list);
        invitationFriendRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        inviteFriendAdapter = new InviteFriendAdapter(inviteFriends);

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
        inviteFriends.add(player2);
        inviteFriends.add(player2);
        inviteFriends.add(player2);
        inviteFriends.add(player2);
        inviteFriends.add(player2);

        invitationFriendRecyclerView.smoothScrollToPosition(0);
        inviteFriendAdapter.notifyDataSetChanged();
        invitationFriendRecyclerView.setAdapter(inviteFriendAdapter);
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    private void startSignInIntent() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        GoogleSignInClient signInClient =  GoogleSignIn.getClient(this.getActivity(), gso);
        Intent intent = signInClient.getSignInIntent();
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 0) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            account = completedTask.getResult(ApiException.class);

            onLogin(account);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    private void onLogin(GoogleSignInAccount account){
        // Signed in successfully, show authenticated UI.
        username.setText(account.getDisplayName());
        MenuActivity.userId = account.getId();


        MenuController.init(MenuActivity.userId);

        JSONObject jsonObject = new JSONObject();
        try {
            //	jsonObject.put("userID", chatUserId.getText().toString());
            jsonObject.put("playerId", account.getId());
            jsonObject.put("username",  account.getDisplayName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MenuController.sendLogin(jsonObject);
    }
}