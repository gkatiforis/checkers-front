package com.katiforis.top10.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import org.json.JSONException;
import org.json.JSONObject;
import com.katiforis.top10.R;
import com.katiforis.top10.controller.MenuController;

import static android.content.ContentValues.TAG;

public class MenuActivity extends Activity {



	private Button play;
	private TextView hi;

	private String user_id;
	private String chat_user_id;
	private static Context context;
	public static String userId = null;

	public static GoogleSignInAccount account;

	private void init() {
		setContentView(R.layout.activity_menu);

		play = findViewById(R.id.send);
		hi = findViewById(R.id.hi);
	}


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = this.getApplicationContext();
		init();

		if(userId == null){
			startSignInIntent();
		}else{
			hi.setText("HI, " + account.getDisplayName());
		}



		play.setOnClickListener(v -> {
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("fromUserID", userId);
				jsonObject.put("gameId", MenuActivity.getGameId());
			} catch (JSONException e) {
				e.printStackTrace();
			}

			MenuController.sendFindGameRequest(jsonObject);
		});



	}

	private void startSignInIntent() {
		// Configure sign-in to request the user's ID, email address, and basic
       // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestEmail()
				.build();

		GoogleSignInClient signInClient =  GoogleSignIn.getClient(this, gso);
		Intent intent = signInClient.getSignInIntent();
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
		//Toast.makeText(this, ( "Welcome, " + account.getDisplayName()), Toast.LENGTH_SHORT).show();

		hi.setText("HI, " + account.getDisplayName());
		userId = account.getId();


		MenuController.init(userId);

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

	public static Context getAppContext(){
		return context;
	}


	public static void saveGameId(String gameId) {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MenuActivity.getAppContext());
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString("gameId", gameId);
		editor.commit();
	}


	public static String getGameId() {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MenuActivity.getAppContext());
		String gamaId = sharedPref.getString("gameId", null);
		return gamaId;
	}
}
