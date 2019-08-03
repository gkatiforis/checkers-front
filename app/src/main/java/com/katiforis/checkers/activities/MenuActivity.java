package com.katiforis.checkers.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.crashlytics.android.Crashlytics;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.katiforis.checkers.DTO.UserDto;
import com.katiforis.checkers.DTO.response.FriendList;
import com.katiforis.checkers.adapter.FriendAdapter;
import com.katiforis.checkers.adapter.ViewPagerAdapter;
import com.katiforis.checkers.R;
import com.katiforis.checkers.controller.HomeController;
import com.katiforis.checkers.fragment.HomeFragment;
import com.katiforis.checkers.fragment.RankFragment;
import com.katiforis.checkers.util.AudioPlayer;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

import static com.katiforis.checkers.conf.Const.TAG;

public class MenuActivity extends AppCompatActivity {
	public static MenuActivity INSTANCE;
	private static Context context;

	private static final int MAIN_MENU_TAB_INDEX = 0;
	private static final int RANK_TAB_INDEX = 1;
	private static final int SHOP_TAB_INDEX = 2;

	public static boolean populated;

	private HomeController homeController;

	private BottomNavigationView bottomNavigationView;
	private ViewPager viewPager;
	private MenuItem prevMenuItem;

	public DrawerLayout drawerLayout;
	public NavigationView navigationView;

    private RecyclerView friendsRecyclerView;
    private FriendAdapter friendAdapter;
    private List<UserDto> friends = new ArrayList<>();
	private Snackbar snack;
	private AudioPlayer audioPlayer;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());
		PendingIntent intent = PendingIntent.getActivity(
				this.getApplication().getBaseContext(),
				0,
				new Intent(getIntent()),
				getIntent().getFlags());

		Thread.setDefaultUncaughtExceptionHandler((Thread thread, Throwable e) ->{
			Log.e(TAG, "Error: ", e);
			if(e instanceof io.reactivex.exceptions.OnErrorNotImplementedException){
				intentToStartPage();
			}else{
				Crashlytics.logException(e);
				AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
				mgr.set(AlarmManager.RTC, System.currentTimeMillis(), intent);
				System.exit(2);
			}
		});

		audioPlayer = new AudioPlayer(this);
		initialize();
	}
	public void intentToStartPage(){
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.setClass(this, StartActivity.class);
		startActivity(intent);
	}
	private void initialize() {



		populated = false;
		INSTANCE = this;
		homeController = HomeController.getInstance();
		homeController.setMenuActivity(this);
		context = this.getApplicationContext();
		getSupportActionBar().hide();
		setContentView(R.layout.activity_menu_layout);



		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		navigationView = findViewById(R.id.navigation_view);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
		bottomNavigationView.setOnNavigationItemSelectedListener(
				new BottomNavigationView.OnNavigationItemSelectedListener() {
					@Override
					public boolean onNavigationItemSelected(@NonNull MenuItem item) {
						switch (item.getItemId()) {
//							case R.id.action_notification:
//								NotificationFragment notificationFragment =	NotificationFragment.getInstance();
//								notificationFragment.show(getSupportFragmentManager(), "dialog");
							case R.id.action_main_menu:
								viewPager.setCurrentItem(MAIN_MENU_TAB_INDEX);
								break;
//							case R.id.action_friend_list:
//								drawerLayout.openDrawer(Gravity.RIGHT);
//								INSTANCE.openFriendListDialog();
//								break;
//							case R.id.action_shop:
//								viewPager.setCurrentItem(SHOP_TAB_INDEX);
//							    break;
							case R.id.action_rank:
								viewPager.setCurrentItem(RANK_TAB_INDEX);
								break;
						}
						return false;
					}
				});

		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				audioPlayer.playPopup();
				if (prevMenuItem != null) {
					prevMenuItem.setChecked(false);
				}
				else
				{
					bottomNavigationView.getMenu().getItem(MAIN_MENU_TAB_INDEX).setChecked(false);
				}
				bottomNavigationView.getMenu().getItem(position).setChecked(true);
				prevMenuItem = bottomNavigationView.getMenu().getItem(position);

				if(position == RANK_TAB_INDEX){
					RankFragment.getInstance().getRankList();
				}else if(position == MAIN_MENU_TAB_INDEX){
					HomeFragment.getInstance().getPlayerDetails();
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

		initViewPager(viewPager);

		snack = Snackbar.make(findViewById(R.id.drawer_layout), "No internet connection. ", Snackbar.LENGTH_INDEFINITE);
		View view = snack.getView();
		FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
		params.gravity = Gravity.TOP;
		view.setLayoutParams(params);
		HomeFragment.getInstance().getPlayerDetails();
	}

	private void initViewPager(ViewPager viewPager) {
		ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
		adapter.addFragment(HomeFragment.getInstance());
		adapter.addFragment(RankFragment.getInstance());
//		adapter.addFragment(new Fragment());
//		adapter.addFragment(LobbyFragment.getInstance());
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(MAIN_MENU_TAB_INDEX);
	}

    private void openFriendListDialog(){

        View view = LayoutInflater.from(this)
                .inflate(R.layout.fragment_friend_list_layout, null, false);
        navigationView.removeAllViews();
        navigationView.addView(view);

        friendsRecyclerView = (RecyclerView) drawerLayout.findViewById(R.id.friend_list);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        friendAdapter = new FriendAdapter(friends);
        friendsRecyclerView.setAdapter(friendAdapter);
        drawerLayout.openDrawer(Gravity.RIGHT);

        if(!populated){
			homeController.getFriendList();
		}
    }

    public void populateFriendListDialog(FriendList friendList){
        runOnUiThread(()->{
            friends.clear();
            friends.addAll(friendList.getPlayers());
            friendsRecyclerView.smoothScrollToPosition(0);
            friendAdapter.notifyDataSetChanged();
        });
		populated = true;
    }

    public void showNoInternetDialog(boolean show){
		if(!show){
			snack.dismiss();
		}
		if(show && !snack.isShown()){
			snack.show();
		}
	}
	public static Context getAppContext(){
		return context;
	}

	@Override
	protected void onStop() {
		super.onStop();

	}

    @Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		audioPlayer.release();
	}

	public AudioPlayer getAudioPlayer() {
		return audioPlayer;
	}
}
