package com.katiforis.checkers.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.katiforis.checkers.BuildConfig;
import com.katiforis.checkers.DTO.PlayerDetailsDto;
import com.katiforis.checkers.DTO.UserDto;
import com.katiforis.checkers.R;
import com.katiforis.checkers.activities.MenuActivity;
import com.katiforis.checkers.activities.StartActivity;
import com.katiforis.checkers.conf.Const;
import com.katiforis.checkers.controller.HomeController;
import com.katiforis.checkers.stomp.Client;
import com.katiforis.checkers.util.AudioPlayer;
import com.katiforis.checkers.util.CircleTransform;
import com.katiforis.checkers.util.LocalCache;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import info.hoang8f.widget.FButton;

import static android.content.ContentValues.TAG;
import static com.katiforis.checkers.util.CachedObjectProperties.TOKEN;
import static com.katiforis.checkers.util.CachedObjectProperties.USER_ID;

public class HomeFragment extends Fragment {
    public static HomeFragment INSTANCE;
    public static GoogleSignInClient signInClient;
    public static boolean populated;
    private HomeController homeController;

    private FButton playButton;
    private TextView loginButton;
    private TextView shareButton;
    //private Button playWithFriend;
    private TextView username;
    private TextView pointsTitle;
    private TextView coinsTitle;
    private ProgressBar playLoading;
    private ImageView playerImage;
    private PieChart pieChart;
    private Button settingButton;


    public static HomeFragment getInstance() {
        if (INSTANCE == null) {
            synchronized (HomeFragment.class) {
                INSTANCE = new HomeFragment();
            }
        }
        INSTANCE.homeController = HomeController.getInstance();
        INSTANCE.homeController.setHomeFragment(INSTANCE);
        return INSTANCE;
    }

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_main_layout, null);

        settingButton = v.findViewById(R.id.settingButton);
        loginButton = v.findViewById(R.id.login);
        shareButton = v.findViewById(R.id.shareButton);
        playButton = (FButton) v.findViewById(R.id.play);
        loginButton.setVisibility(View.GONE);
        //playWithFriend = v.findViewById(R.id.play_with_friend);
        username = v.findViewById(R.id.username);
        pointsTitle = v.findViewById(R.id.pointsTitle);
        coinsTitle = v.findViewById(R.id.coinsTitle);
        playerImage = v.findViewById(R.id.playerImage);

        pieChart = v.findViewById(R.id.user_lvl_chart);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);

        pieChart.setHoleColor(Color.TRANSPARENT);

        pieChart.setHoleRadius(70);

        playButton.setButtonColor(getResources().getColor(R.color.fbutton_color_nephritis));


        loginButton.setOnClickListener(p -> {
            AudioPlayer.getInstance(MenuActivity.INSTANCE).playClickButton();
            signInIntent();
        });

        shareButton.setOnClickListener(p -> {
            AudioPlayer.getInstance(MenuActivity.INSTANCE).playClickButton();
            shareGame();
        });

        settingButton.setOnClickListener(p -> {
            SettingsFragment settingsFragment = SettingsFragment.getInstance();
            settingsFragment.show(this.getFragmentManager(), "");
        });

        buttonEffect(settingButton);
        playButton.setOnClickListener(p -> {
            AudioPlayer.getInstance(MenuActivity.INSTANCE).playClickButton();
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

    public void shareGame() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Checkers Multiplayer");
        String shareMessage = "Let me recommend you this cool multiplayer checkers game on Google Play: \n\n";
        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n Have fun!";
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        startActivity(Intent.createChooser(shareIntent, "Share"));
    }

    public void logout() {
        signInClient.signOut()
                .addOnSuccessListener((message) -> {
                    Client.getInstance().disconnect();
                    LocalCache.getInstance().saveString(TOKEN, null);
                    LocalCache.getInstance().saveString(USER_ID, null);
                    intentToStartPage();
                })
                .addOnFailureListener((message) -> {
                    //TODO show message
                });
    }


    public static void buttonEffect(View button) {
        button.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
                v.startAnimation(buttonClick);
                final Animation animShake = AnimationUtils.loadAnimation(MenuActivity.INSTANCE, R.anim.shake);
                v.startAnimation(animShake);
                return false;
            }
        });
    }


    private void signInIntent() {
        Intent intent = signInClient.getSignInIntent();
        startActivityForResult(intent, 0);
    }

    private void signInAgain() {
        String token = (String) LocalCache.getInstance().getString(TOKEN);
        if (token != null) {
            intentToStartPage();
        }
    }

    private void intentToStartPage() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setClass(MenuActivity.getAppContext(), StartActivity.class);
        startActivity(intent);
    }

    private void silentSignIn() {
        Task<GoogleSignInAccount> task = signInClient.silentSignIn();
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if (account != null) {
                onLogin(account);
            } else {
                signInAgain();
            }
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            signInAgain();
        } catch (Exception e) {
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
                if (account != null) {
                    onLogin(account);
                } else {
                    //TODO repost exception to usesr
                    loginButton.setVisibility(View.VISIBLE);
                }
            } catch (ApiException e) {
                Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
                loginButton.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                //TODO repost exception to usesr
                Log.w(TAG, "signInResult:failed ", e);
                loginButton.setVisibility(View.VISIBLE);
            }
        }
    }

    private void onLogin(GoogleSignInAccount account) {
        // logout.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.GONE);
        LocalCache.getInstance().saveString(TOKEN, account.getIdToken());
        if (Client.isConnected()) {
            Client.getInstance().disconnect();
            Client.getInstance();
            getPlayerDetails();
        } else {
            Client.getInstance();
        }
    }

    public void getPlayerDetails() {
        if (!populated) {
            homeController.getPlayerDetails();
            populated = true;
        } else {
            homeController.getPlayerDetailsIfExpired();
        }
    }

    public void populatePlayerDetails(UserDto playerDto) {
        getActivity().runOnUiThread(() -> {
            if (playerDto.getUserId().startsWith("guest_")) {
                loginButton.setVisibility(View.VISIBLE);
            }
            username.setText(playerDto.getUsername());
            PlayerDetailsDto playerDetailsDto = playerDto.getPlayerDetails();
            int lvl = playerDto.getPlayerDetails().getLevel();
            pieChart.setCenterText("LVL " + lvl);
            List<PieEntry> exps = new ArrayList();

            float exp = playerDto.getPlayerDetails().getLevelPoints();
            float maxExp = lvl * 20;

            float per = exp / maxExp * 100;
            if (per < 5) {
                per = 5;
            }
            exps.add(new PieEntry(per));
            exps.add(new PieEntry(100 - per));

            final int[] MY_COLORS = {
                    getActivity().getResources().getColor(R.color.fbutton_color_pomegranate),
                    Color.TRANSPARENT
            };
            ArrayList<Integer> colors = new ArrayList<>();

            for (int c : MY_COLORS) colors.add(c);
            PieDataSet dataSet = new PieDataSet(exps, "");
            dataSet.setColors(colors);
            PieData data = new PieData(dataSet);
            pieChart.setData(data);
            pieChart.animateXY(1000, 1000);
            coinsTitle.setText(String.valueOf(playerDetailsDto.getCoins()));
            pointsTitle.setText(String.valueOf(playerDetailsDto.getElo()));
            Picasso.with(getActivity())
                    .load(playerDto.getPictureUrl())
                    .placeholder(getResources().getDrawable(R.drawable.user))
                    .transform(new CircleTransform())

                    .error(R.mipmap.ic_launcher)

                    .into(playerImage, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                            //TODO
                        }
                    });
        });
    }
}