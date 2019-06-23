package com.katiforis.checkers.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
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
import com.katiforis.checkers.DTO.PlayerDetailsDto;
import com.katiforis.checkers.DTO.UserDto;
import com.katiforis.checkers.R;
import com.katiforis.checkers.activities.MenuActivity;
import com.katiforis.checkers.activities.StartActivity;
import com.katiforis.checkers.conf.Const;
import com.katiforis.checkers.controller.HomeController;
import com.katiforis.checkers.stomp.Client;
import com.katiforis.checkers.util.LocalCache;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import static android.content.ContentValues.TAG;
import static com.katiforis.checkers.util.CachedObjectProperties.TOKEN;
import static com.katiforis.checkers.util.CachedObjectProperties.USER_ID;

public class HomeFragment extends Fragment {
    public static HomeFragment INSTANCE;
    public static GoogleSignInClient signInClient;
    public static boolean populated;
    private HomeController homeController;

    private Button logout;
    private Button play;
    private Button login;
    //private Button playWithFriend;
    private TextView username;
    private TextView lvlTitle;
    private TextView pointsTitle;
    private TextView coinsTitle;

    private ImageView playerImage;



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
        login = v.findViewById(R.id.login);
        logout.setVisibility(View.GONE);
        login.setVisibility(View.GONE);
        //playWithFriend = v.findViewById(R.id.play_with_friend);
        username = v.findViewById(R.id.username);
        lvlTitle = v.findViewById(R.id.lvlTitle);
        pointsTitle = v.findViewById(R.id.pointsTitle);
        coinsTitle = v.findViewById(R.id.coinsTitle);
        playerImage =  v.findViewById(R.id.playerImage);

        login.setOnClickListener(p -> {
            signInIntent();
        });

        logout.setOnClickListener(p -> {
            signInClient.signOut()
                 .addOnSuccessListener((message)->{
                     Client.getInstance().disconnect();
                     LocalCache.getInstance().saveString(TOKEN, null);
                     LocalCache.getInstance().saveString(USER_ID, null);
                        intentToStartPage();
                    })
                .addOnFailureListener((message)->{
                    //TODO show message
                });
        });

        play.setOnClickListener(p -> {
		   homeController.findGame();
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Const.android_web_id)
                .requestEmail()
                .build();
        signInClient = GoogleSignIn.getClient(this.getActivity(), gso);

        silentSignIn();
        return v;
    }

    private void signInIntent() {
        Intent intent = signInClient.getSignInIntent();
        startActivityForResult(intent, 0);
    }

    private void signInAgain() {
        String token = (String) LocalCache.getInstance().getString(TOKEN);
        if(token != null){
            intentToStartPage();
        }
    }

    private void intentToStartPage(){
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setClass(MenuActivity.getAppContext(), StartActivity.class);
        startActivity(intent);
    }

    private void silentSignIn() {
        Task<GoogleSignInAccount> task = signInClient.silentSignIn();
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if(account != null){
                onLogin(account);
            }else{
                signInAgain();
            }
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            signInAgain();
        }catch (Exception e){
            Log.w(TAG, "signInResult:failed ", e);
            signInAgain();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if(account != null){
                    onLogin(account);
                }else{
                    //TODO repost exception to usesr
                    login.setVisibility(View.VISIBLE);
                }
            } catch (ApiException e) {
                Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
                login.setVisibility(View.VISIBLE);
            }catch (Exception e){
                //TODO repost exception to usesr
                Log.w(TAG, "signInResult:failed ", e);
                login.setVisibility(View.VISIBLE);
            }
        }
    }

    private void onLogin(GoogleSignInAccount account){
         logout.setVisibility(View.VISIBLE);
         login.setVisibility(View.GONE);
        LocalCache.getInstance().saveString(TOKEN,account.getIdToken());
        if(Client.isConnected()){
            Client.getInstance().disconnect();
            Client.getInstance();
            getPlayerDetails();
        }else{
            Client.getInstance();
        }
    }

    public void getPlayerDetails(){
        if(!populated){
            homeController.getPlayerDetails();
            populated = true;
        }else{
            homeController.getPlayerDetailsIfExpired();
        }
    }

    public void populatePlayerDetails(UserDto playerDto){
        getActivity().runOnUiThread(() -> {
            if(playerDto.getUserId().startsWith("guest_")){
                login.setVisibility(View.VISIBLE);
            }
            username.setText(playerDto.getUsername());
            PlayerDetailsDto playerDetailsDto = playerDto.getPlayerDetails();
            lvlTitle.setText(String.valueOf(playerDetailsDto.getLevel()));
            coinsTitle.setText(String.valueOf(playerDetailsDto.getCoins()));
            pointsTitle.setText(String.valueOf(playerDetailsDto.getElo()));
            Picasso.with(getActivity())
                    .load(playerDto.getPictureUrl())
                    .error(R.mipmap.ic_launcher)
                    .into(playerImage, new Callback() {
                        @Override
                        public void onSuccess() {     }

                        @Override
                        public void onError() {
                            //TODO
                        }
                    });
        });
    }
}