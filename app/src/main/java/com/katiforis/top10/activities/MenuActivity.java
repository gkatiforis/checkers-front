package com.katiforis.top10.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
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

import com.katiforis.top10.DTO.Player;
import com.katiforis.top10.adapter.FriendAdapter;
import com.katiforis.top10.adapter.ViewPagerAdapter;
import com.katiforis.top10.R;
import com.katiforis.top10.fragment.LobbyFragment;
import com.katiforis.top10.fragment.MainFragment;
import com.katiforis.top10.fragment.NotificationFragment;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {
	public MenuActivity instance;

	private BottomNavigationView bottomNavigationView;
	private ViewPager viewPager;
	private MenuItem prevMenuItem;

	private static Context context;
	public static String userId = null;

	public DrawerLayout drawerLayout;
	public NavigationView navigationView;

    private RecyclerView friendsRecyclerView;
    private FriendAdapter friendAdapter;
    private List<Player> friends = new ArrayList<>();

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		initialize();
	}

	private void openFriendListDialog(){
		navigationView.removeAllViews();

		View v = LayoutInflater.from(this)
				.inflate(R.layout.fragment_friend_list_layout, null, false);
		navigationView.addView(v);

		friendsRecyclerView = (RecyclerView) drawerLayout.findViewById(R.id.friend_list);
		friendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		friendAdapter = new FriendAdapter(friends);

		Player player = new Player(1l, "id","gkatiforis", "George Katiforis", 100, 1);
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
		friendsRecyclerView.smoothScrollToPosition(0);
		friendAdapter.notifyDataSetChanged();
		friendsRecyclerView.setAdapter(friendAdapter);

	}
	private void initialize() {
		instance = this;
		context = this.getApplicationContext();
		getSupportActionBar().hide();
		setContentView(R.layout.activity_menu_layout);

		drawerLayout = (DrawerLayout) findViewById(R.id.activity_mainn);
		navigationView = findViewById(R.id.nv);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
		bottomNavigationView.setOnNavigationItemSelectedListener(
				new BottomNavigationView.OnNavigationItemSelectedListener() {
					@Override
					public boolean onNavigationItemSelected(@NonNull MenuItem item) {
						switch (item.getItemId()) {
							case R.id.action_notification:
								NotificationFragment notificationFragment =	NotificationFragment.newInstance(4);
								notificationFragment.show(getSupportFragmentManager(), "dialog");
							case R.id.action_main_menu:
								viewPager.setCurrentItem(2);
								break;
							case R.id.action_friend_list:
								drawerLayout.openDrawer(Gravity.RIGHT);
								instance.openFriendListDialog();
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

	}

	private void initViewPager(ViewPager viewPager) {
		ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
		adapter.addFragment(new android.support.v4.app.Fragment());
		adapter.addFragment(new android.support.v4.app.Fragment());
		adapter.addFragment(MainFragment.newInstance(4));
		adapter.addFragment(LobbyFragment.newInstance(4));
		adapter.addFragment(new android.support.v4.app.Fragment());
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(2);
	}
	public static Context getAppContext(){
		return context;
	}
}
