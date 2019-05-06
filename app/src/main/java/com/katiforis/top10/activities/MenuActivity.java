package com.katiforis.top10.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.katiforis.top10.DTO.response.FriendList;
import com.katiforis.top10.DTO.Player;
import com.katiforis.top10.adapter.FriendAdapter;
import com.katiforis.top10.adapter.ViewPagerAdapter;
import com.katiforis.top10.R;
import com.katiforis.top10.controller.HomeController;
import com.katiforis.top10.fragment.LobbyFragment;
import com.katiforis.top10.fragment.HomeFragment;
import com.katiforis.top10.fragment.RankFragment;
import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {
	public static MenuActivity INSTANCE;
	private static Context context;

	private static final int MAIN_MENU_TAB_INDEX = 1;
	private static final int RANK_TAB_INDEX = 0;
	private static final int SHOP_TAB_INDEX = 2;

	public static String userId = null;
	public static boolean populated;

	private HomeController homeController;

	private BottomNavigationView bottomNavigationView;
	private ViewPager viewPager;
	private MenuItem prevMenuItem;

	public DrawerLayout drawerLayout;
	public NavigationView navigationView;

    private RecyclerView friendsRecyclerView;
    private FriendAdapter friendAdapter;
    private List<Player> friends = new ArrayList<>();

	Snackbar snack;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		initialize();
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
							case R.id.action_shop:
								viewPager.setCurrentItem(SHOP_TAB_INDEX);
							    break;
							case R.id.action_rank:
								viewPager.setCurrentItem(RANK_TAB_INDEX);
								RankFragment.getInstance().getRankList();
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
				if (prevMenuItem != null) {
					prevMenuItem.setChecked(false);
				}
				else
				{
					bottomNavigationView.getMenu().getItem(0).setChecked(false);
				}
				Log.d("page", "onPageSelected: "+position);
				bottomNavigationView.getMenu().getItem(position).setChecked(true);
				prevMenuItem = bottomNavigationView.getMenu().getItem(position);

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

		initViewPager(viewPager);

		snack = Snackbar.make(findViewById(R.id.drawer_layout), "No internet connetion. ", Snackbar.LENGTH_INDEFINITE);
		View view = snack.getView();
		FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
		params.gravity = Gravity.TOP;
		view.setLayoutParams(params);

	}

	private void initViewPager(ViewPager viewPager) {
		ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
		adapter.addFragment(RankFragment.getInstance());
		adapter.addFragment(HomeFragment.getInstance());
		adapter.addFragment(new android.support.v4.app.Fragment());
		adapter.addFragment(LobbyFragment.getInstance());
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(1);
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
}
