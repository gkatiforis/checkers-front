package com.katiforis.top10.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.katiforis.top10.adapter.ViewPagerAdapter;
import com.katiforis.top10.fragment.FriendListFragment;
import com.katiforis.top10.R;
import com.katiforis.top10.fragment.MainFragment;

public class MenuActivity extends AppCompatActivity {

	private BottomNavigationView bottomNavigationView;
	private ViewPager viewPager;
	private MenuItem prevMenuItem;

	private String user_id;
	private String chat_user_id;
	private static Context context;
	public static String userId = null;

//	private BubblesManager bubblesManager;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		initialize();
	}

	private void initialize() {
		context = this.getApplicationContext();
		getSupportActionBar().hide();
		setContentView(R.layout.activity_menu_layout);

		viewPager = (ViewPager) findViewById(R.id.viewpager);
		bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
		bottomNavigationView.setOnNavigationItemSelectedListener(
				new BottomNavigationView.OnNavigationItemSelectedListener() {
					@Override
					public boolean onNavigationItemSelected(@NonNull MenuItem item) {
						switch (item.getItemId()) {
							case R.id.action_main_menu:
								viewPager.setCurrentItem(0);
								break;
							case R.id.action_friend_list:
								viewPager.setCurrentItem(1);
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
		adapter.addFragment(MainFragment.newInstance(4));
		adapter.addFragment(FriendListFragment.newInstance(4));
		adapter.addFragment(FriendListFragment.newInstance(4));
		viewPager.setAdapter(adapter);
	}

	public static Context getAppContext(){
		return context;
	}
}
