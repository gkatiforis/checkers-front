package com.katiforis.top10.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.katiforis.top10.DTO.PlayerDetails;
import com.katiforis.top10.R;
import com.katiforis.top10.activities.GameActivity;
import com.katiforis.top10.activities.MenuActivity;
import com.katiforis.top10.controller.HomeController;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment {
    public static HomeFragment INSTANCE;

    private HomeController homeController;
    public static GoogleSignInAccount account;

    private Button logout;
    private Button play;
    private Button playWithFriend;
    private TextView username;
    private ImageView playerImage;

    GoogleSignInClient signInClient;

    public static HomeFragment getInstance() {
        if (INSTANCE == null) {
            synchronized(HomeFragment.class) {
                INSTANCE = new HomeFragment();
            }
        }
        INSTANCE.homeController = HomeController.getInstance();
        INSTANCE.homeController.setHomeFragment(INSTANCE);
        return INSTANCE;
    }

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_main_layout,  null);

        logout = v.findViewById(R.id.logout);
        play = v.findViewById(R.id.play);
        playWithFriend = v.findViewById(R.id.play_with_friend);
        username = v.findViewById(R.id.username);
        playerImage =  v.findViewById(R.id.playerImage);
        if(MenuActivity.userId == null){
            startSignInIntent();
        }else{
            username.setText(account.getDisplayName());

        }

        logout.setOnClickListener(p -> {
            signInClient.signOut();
        });

       // LocalCache.getInstance().save(this.getActivity());
        //LocalCache.getInstance().load(this.getActivity());
        play.setOnClickListener(p -> {
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("fromUserID", MenuActivity.userId);
				jsonObject.put("gameId", GameActivity.getGameId());
			} catch (JSONException e) {
				e.printStackTrace();
			}

		   homeController.findGame(jsonObject);
        });

        return v;
    }

    private void startSignInIntent() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        signInClient = GoogleSignIn.getClient(this.getActivity(), gso);
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
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void onLogin(GoogleSignInAccount account){
        username.setText(account.getDisplayName());

        Picasso.with(this.getActivity())
                .load(account.getPhotoUrl().toString()).error(R.mipmap.ic_launcher)
                .into(playerImage, new Callback() {
            @Override
            public void onSuccess() {     }

            @Override
            public void onError() {
               //TODO
            }
        });

        MenuActivity.userId = account.getId();

        JSONObject jsonObject = new JSONObject();
        try {
            //	jsonObject.put("userID", chatUserId.getText().toString());
            jsonObject.put("playerId", account.getId());
            jsonObject.put("username",  account.getDisplayName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HomeController.getInstance().login(jsonObject);
    }

    public void populatePlayerDetails(PlayerDetails playerDetails){
        getActivity().runOnUiThread(() -> {
            username.setText(playerDetails.getUsername());
        });
    }
}