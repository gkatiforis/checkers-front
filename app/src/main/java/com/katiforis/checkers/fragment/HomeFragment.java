package com.katiforis.checkers.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.katiforis.checkers.BuildConfig;
import com.katiforis.checkers.DTO.GameType;
import com.katiforis.checkers.DTO.LevelEnum;
import com.katiforis.checkers.DTO.PlayerDetailsDto;
import com.katiforis.checkers.DTO.RewardEnum;
import com.katiforis.checkers.DTO.UserDto;
import com.katiforis.checkers.DTO.request.FindGame;
import com.katiforis.checkers.DTO.request.Reward;
import com.katiforis.checkers.R;
import com.katiforis.checkers.activities.MenuActivity;
import com.katiforis.checkers.activities.StartActivity;
import com.katiforis.checkers.conf.Const;
import com.katiforis.checkers.controller.HomeController;
import com.katiforis.checkers.stomp.Client;
import com.katiforis.checkers.util.CircleTransform;
import com.katiforis.checkers.util.LocalCache;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;
import info.hoang8f.widget.FButton;

import static android.content.ContentValues.TAG;
import static android.os.Looper.getMainLooper;
import static com.katiforis.checkers.DTO.LevelEnum.LEVEL_1;
import static com.katiforis.checkers.DTO.LevelEnum.LEVEL_2;
import static com.katiforis.checkers.DTO.LevelEnum.LEVEL_3;
import static com.katiforis.checkers.DTO.LevelEnum.LEVEL_4;
import static com.katiforis.checkers.DTO.LevelEnum.LEVEL_5;
import static com.katiforis.checkers.DTO.LevelEnum.LEVEL_6;
import static com.katiforis.checkers.DTO.LevelEnum.mapPointsToLevel;
import static com.katiforis.checkers.conf.Const.APP_AD_ID;
import static com.katiforis.checkers.conf.Const.INTERSTITIAL_AD_ID;
import static com.katiforis.checkers.conf.Const.REWARD_AD_ID;
import static com.katiforis.checkers.util.CachedObjectProperties.TOKEN;
import static com.katiforis.checkers.util.CachedObjectProperties.USER_DETAILS;
import static com.katiforis.checkers.util.CachedObjectProperties.USER_ID;

public class HomeFragment extends Fragment {
    private MenuActivity menuActivity;

    public static boolean populated;
    private HomeController homeController;

    private FButton playRanking;
    private FButton searchingForOpponent;
    private TextView loginButton;
    private FButton shareButton;
    private FButton showAdButton;
    //private Button playWithFriend;
    private TextView username;
    private TextView pointsTitle;
    private ImageView pointsImage;
    private TextView coinsTitle;
    private ProgressBar playLoading;
    private View userPanel;
    private ImageView playerImage;
    private PieChart pieChart;
    private Button settingButton;
    private AdView mAdView;
    private InterstitialAd interstitialAd;
    private RewardedAd rewardedAd;
    private final int interstitialPossibility = 10;
    private BillingClient billingClient;
    public static GoogleSignInClient signInClient;
    private GoogleSignInAccount account = null;
    boolean rewardEarned = false;

    public HomeFragment() {
    }

    public HomeFragment(HomeController homeController) {
        this.homeController = homeController;
    }

    AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener = billingResult -> {
        System.out.println("sdfsf");
    };

    void handlePurchase(Purchase purchase) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams =
                        AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build();
                billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_main_layout, null);

        menuActivity = (MenuActivity) getActivity();
        MobileAds.initialize(this.getActivity(), APP_AD_ID);

        mAdView = v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        interstitialAd = new InterstitialAd(this.getActivity());
        interstitialAd.setAdUnitId(INTERSTITIAL_AD_ID);
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
            }

            @Override
            public void onAdOpened() {
            }

            @Override
            public void onAdClicked() {
            }

            @Override
            public void onAdLeftApplication() {
            }

            @Override
            public void onAdClosed() {
                interstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

        rewardedAd = createAndLoadRewardedAd(REWARD_AD_ID);

        userPanel = v.findViewById(R.id.user_panel);
        settingButton = v.findViewById(R.id.settingButton);
        loginButton = v.findViewById(R.id.login);
        shareButton = v.findViewById(R.id.shareButton);
        showAdButton = v.findViewById(R.id.showAdButton);
        playRanking = (FButton) v.findViewById(R.id.playRanking);
        searchingForOpponent = (FButton) v.findViewById(R.id.searchingForOpponent);
        loginButton.setVisibility(View.GONE);
        //playWithFriend = v.findViewById(R.id.play_with_friend);
        username = v.findViewById(R.id.username);
        pointsTitle = v.findViewById(R.id.pointsTitle);
        pointsImage = v.findViewById(R.id.pointsImage);
        coinsTitle = v.findViewById(R.id.coinsTitle);
        playerImage = v.findViewById(R.id.playerImage);

        pieChart = v.findViewById(R.id.user_lvl_chart);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);

        pieChart.setHoleColor(Color.TRANSPARENT);

        pieChart.setHoleRadius(70);

        playRanking.setButtonColor(getResources().getColor(R.color.fbutton_color_nephritis));
        searchingForOpponent.setButtonColor(getResources().getColor(R.color.fbutton_color_nephritis));


        loginButton.setOnClickListener(p -> {
            menuActivity.getAudioPlayer().playClickButton();
            playRanking.setVisibility(View.VISIBLE);
            searchingForOpponent.setVisibility(View.GONE);
            signInIntent();
        });


        PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
                purchases = purchases;
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                        && purchases != null) {
                    for (Purchase purchase : purchases) {
                        handlePurchase(purchase);
                    }
                } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                } else {
                }
            }
        };


        billingClient = BillingClient.newBuilder(this.getActivity()).setListener(purchasesUpdatedListener).enablePendingPurchases().build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
            }
        });
        shareButton.setOnClickListener(p -> {
//            AudioPlayer.getInstance(MenuActivity.INSTANCE).playClickButton();
//            shareGame();


            List<String> skuList = new ArrayList<>();
            skuList.add("prov");
            skuList.add("pro_version");
            SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
            params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
            billingClient.querySkuDetailsAsync(params.build(),
                    new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(BillingResult billingResult,
                                                         List<SkuDetails> skuDetailsList) {
                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                                for (SkuDetails skuDetails : skuDetailsList) {
                                    String sku = skuDetails.getSku();
                                    String price = skuDetails.getPrice();
                                    if ("premium_upgrade".equals(sku)) {
                                        // premiumUpgradePrice = price;
                                    } else if ("gas".equals(sku)) {
                                        //  gasPrice = price;
                                    }
                                }
                            }
                        }
                    });

        });

        showAdButton.setOnClickListener(p -> {
            menuActivity.getAudioPlayer().playClickButton();
            showAdVideo();
        });

        settingButton.setOnClickListener(p -> {
            SettingsFragment settingsFragment = SettingsFragment.getInstance();
            settingsFragment.show(this.getFragmentManager(), "");
        });

        playRanking.setOnClickListener(p -> {
            menuActivity.getAudioPlayer().playClickButton();
            UserDto playerDto = LocalCache.getInstance().get(USER_DETAILS, this.getActivity());

            if (playerDto == null) return;
            if (playerDto.getPlayerDetails().getCoins() < GameType.RANKING.getFee()) {
                showNotEnoughCoins();
            } else {
                FindGame findGame = new FindGame();
                findGame.setGameType(GameType.RANKING);
                homeController.findGame(findGame)
                        .subscribe(
                                () -> {
//                            explosionField.explode(playRanking);
                                    // explosionField.clear();
                                    playRanking.setVisibility(View.GONE);
                                    searchingForOpponent.setVisibility(View.VISIBLE);
                                    //TODO
                                },
                                throwable -> {
                                });
            }
        });

        searchingForOpponent.setOnClickListener(p -> {
            menuActivity.getAudioPlayer().playClickButton();
            FindGame findGame = new FindGame();
            findGame.setCancelSearching(true);
            homeController.findGame(findGame)
                    .subscribe(
                            () -> {
                                playRanking.setVisibility(View.VISIBLE);
                                searchingForOpponent.setVisibility(View.GONE);
                            },
                            throwable -> {
                            });

        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Const.android_web_id)
                .requestEmail()
                .build();

        if (signInClient == null) {
            signInClient = GoogleSignIn.getClient(this.getActivity(), gso);
            silentSignIn();
        } else {
            getPlayerDetailsForce();
        }

        Client.getInstance().registerObserver((MenuActivity) this.getActivity());
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


    public void showAdVideo() {

        if (rewardedAd.isLoaded()) {
            RewardedAdCallback adCallback = new RewardedAdCallback() {
                @Override
                public void onRewardedAdOpened() {
                }

                @Override
                public void onRewardedAdClosed() {
                    if (rewardEarned) {
                        rewardEarned = false;
                        showAdRewardSuccessDialog();
                    } else {
                        showAdRewardFailDialog();
                    }
                    rewardedAd = createAndLoadRewardedAd(REWARD_AD_ID);
                }

                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    rewardEarned = true;
                    Reward reward = new Reward();
                    reward.setType(rewardItem.getType());
                    reward.setAmount(RewardEnum.VIDEO.getCoinsPrize());
                    homeController.sendReward(reward);
                    UserDto userDto = LocalCache.getInstance().get(USER_DETAILS, menuActivity);
                    userDto.getPlayerDetails().setCoins(userDto.getPlayerDetails().getCoins() + reward.getAmount());
                    LocalCache.getInstance().save(userDto, USER_DETAILS, menuActivity);
                    homeController.getPlayerDetails();
                }

                @Override
                public void onRewardedAdFailedToShow(int errorCode) {
                    showAdRewardFailDialog();
                }
            };
            rewardedAd.show(menuActivity, adCallback);
        } else {
            Log.d("TAG", "The rewarded ad wasn't loaded yet.");
        }
    }


    public void showAdRewardSuccessDialog() {
        new SweetAlertDialog(menuActivity, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Congratulations!")
                .setContentText("You have earned " + RewardEnum.VIDEO.getCoinsPrize() + " coins!")
                .show();
    }

    public void showAdRewardFailDialog() {
        new SweetAlertDialog(menuActivity, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Error")
                .setContentText("Please try again later.")
                .show();
    }

    public void showNotEnoughCoins() {
        new SweetAlertDialog(menuActivity, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Not enough coins!")
                .setContentText("Watch an ad and earn more coins!!")
                .show();
    }


    public RewardedAd createAndLoadRewardedAd(String adUnitId) {
        RewardedAd rewardedAd = new RewardedAd(this.getActivity(), adUnitId);
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        return rewardedAd;
    }


    public void logout() {
        signInClient.signOut()
                .addOnSuccessListener((message) -> {
                    Client.getInstance().disconnect();
                    if(!LocalCache.getInstance().getString(USER_ID, this.getActivity()).startsWith("guest_")){
                    LocalCache.getInstance().saveString(TOKEN, null, this.getActivity());

                        LocalCache.getInstance().saveString(USER_ID, null, this.getActivity());
                    }

                    intentToStartPage();
                })
                .addOnFailureListener((message) -> {
                    //TODO show message
                });
    }

    private void signInIntent() {
        Intent intent = signInClient.getSignInIntent();
        startActivityForResult(intent, 0);
    }

    private void signInAgain() {
        String token = (String) LocalCache.getInstance().getString(TOKEN, menuActivity);
        if (token != null) {
            intentToStartPage();
        } else {
            getPlayerDetailsForce();
        }
    }

    private void intentToStartPage() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setClass(menuActivity, StartActivity.class);
        startActivity(intent);
    }

    private void silentSignIn() {

        signInClient.silentSignIn().addOnCompleteListener(this.getActivity(),
                new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInSilently(): success");
                            onLogin(task.getResult());
                        } else {
                            Log.d(TAG, "signInSilently(): failure", task.getException());
                            signInAgain();
                            //signInIntent();
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                account = task.getResult(ApiException.class);
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
        LocalCache.getInstance().saveString(TOKEN, account.getIdToken(), menuActivity);
        if (Client.isConnected()) {
            Client.getInstance().disconnect();
        }
        Client.getInstance().registerObserver((MenuActivity) this.getActivity());
        getPlayerDetailsForce();
    }

    public void getPlayerDetailsForce() {
        LocalCache.getInstance().save(null, USER_DETAILS, this.getActivity());
        homeController.getPlayerDetails();
        populated = true;
    }


    public void getPlayerDetails() {
        if (!populated) {
            homeController.getPlayerDetails();
            populated = true;
        } else {
            homeController.getPlayerDetailsIfExpired();
        }
    }

    private void showInterstitial() {
        Random rand = new Random();
        int randomNum = rand.nextInt((100 - 1) + 1) + 1;

        if (randomNum <= interstitialPossibility) {
            final Handler handler = new Handler(getMainLooper());
            if (interstitialAd != null && interstitialAd.isLoaded()) {
                interstitialAd.show();
            } else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (interstitialAd != null && interstitialAd.isLoaded()) {
                            interstitialAd.show();
                        } else {
                            handler.postDelayed(this, 1000);
                        }
                    }
                }, 1000);
            }
        }
    }

    public void populatePlayerDetails(UserDto playerDto) {
        getActivity().runOnUiThread(() -> {

            userPanel.setVisibility(View.VISIBLE);
            showInterstitial();

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
            coinsTitle.setText(String.valueOf(playerDetailsDto.getCoins() + " coins"));
            pointsTitle.setText(String.valueOf(playerDetailsDto.getElo() + " elo"));
            LevelEnum level = mapPointsToLevel(playerDetailsDto.getElo());
            if (level == LEVEL_1)
                pointsImage.setImageResource(R.drawable.level1);
            else if (level == LEVEL_2)
                pointsImage.setImageResource(R.drawable.level2);
            else if (level == LEVEL_3)
                pointsImage.setImageResource(R.drawable.level3);
            else if (level == LEVEL_4)
                pointsImage.setImageResource(R.drawable.level4);
            else if (level == LEVEL_5)
                pointsImage.setImageResource(R.drawable.level5);
            else if (level == LEVEL_6)
                pointsImage.setImageResource(R.drawable.level6);
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