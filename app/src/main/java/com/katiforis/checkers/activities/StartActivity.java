package com.katiforis.checkers.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.katiforis.checkers.R;
import com.katiforis.checkers.fragment.HomeFragment;
import com.katiforis.checkers.stomp.Client;
import com.katiforis.checkers.util.AudioPlayer;
import com.katiforis.checkers.util.LocalCache;

import info.hoang8f.widget.FButton;

import static android.content.ContentValues.TAG;
import static com.katiforis.checkers.util.CachedObjectProperties.TOKEN;
import static com.katiforis.checkers.util.CachedObjectProperties.USER_ID;

public class StartActivity extends AppCompatActivity {
	public static StartActivity INSTANCE;
	private static Context context;
	public static String userId = null;
    private FButton loginWithGoogle;
	private FButton loginAsGuest;
	private Snackbar snack;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		initialize();
	}

	private void initialize() {
		INSTANCE = this;
		context = this.getApplicationContext();
		getSupportActionBar().hide();
		setContentView(R.layout.activity_start_layout);
		loginAsGuest = findViewById(R.id.login_as_guest);
		loginWithGoogle = findViewById(R.id.login_with_google);
		loginWithGoogle.setButtonColor(getResources().getColor(R.color.fbutton_color_pomegranate));
		loginAsGuest.setButtonColor(getResources().getColor(R.color.fbutton_color_silver2));
		loginAsGuest.setOnClickListener(p -> {
			AudioPlayer.getInstance(this).playClickButton();
			LocalCache.getInstance().saveString(TOKEN, null);
			LocalCache.getInstance().saveString(USER_ID, null);
			intentToMenuActivity();
		});
		loginWithGoogle.setOnClickListener(p -> {
			AudioPlayer.getInstance(this).playClickButton();
			signInIntent();
		});
	}

	private void intentToMenuActivity(){
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.setClass(this, MenuActivity.class);
		startActivity(intent);
	}

	private void signInIntent() {
		Intent intent = HomeFragment.signInClient.getSignInIntent();
		startActivityForResult(intent, 0);
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
				}
			} catch (ApiException e) {
				Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
			}catch (Exception e){
				//TODO repost exception to usesr
				Log.w(TAG, "signInResult:failed ", e);
			}
		}
	}

	private void onLogin(GoogleSignInAccount account){
		LocalCache.getInstance().saveString(TOKEN, account.getIdToken());
		if(Client.isConnected()){
			Client.getInstance().disconnect();
			Client.getInstance();
		}else{
			Client.getInstance();
		}
		intentToMenuActivity();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}
}
