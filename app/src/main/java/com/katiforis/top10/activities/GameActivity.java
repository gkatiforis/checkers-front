package com.katiforis.top10.activities;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.katiforis.top10.DTO.Answer;
import com.katiforis.top10.DTO.UserDto;
import com.katiforis.top10.DTO.response.GameState;
import com.katiforis.top10.DTO.PlayerAnswer;
import com.katiforis.top10.DTO.Question;
import com.katiforis.top10.R;
import com.katiforis.top10.adapter.AnswerAdapter;
import com.katiforis.top10.conf.Const;
import com.katiforis.top10.controller.GameController;
import com.katiforis.top10.game.QuestionHandler;
import com.katiforis.top10.game.QuestionHandlerImpl;
import com.katiforis.top10.speech.SpeechRecognizerManager;
import com.katiforis.top10.adapter.PlayerAdapter;
import com.katiforis.top10.util.LocalCache;

import tyrantgit.explosionfield.ExplosionField;

import static com.katiforis.top10.util.CachedObjectProperties.CURRENT_GAME_ID;

public class GameActivity extends AppCompatActivity {

    public static GameActivity INSTANCE;
	long currentQuestionId;

	ProgressBar mProgressBar;
	CountDownTimer mCountDownTimer;
	int timerStep=0;
	Date startDate;
	Date currentDat;
	long dateStarted;
	long dateCurrent;

	private EditText answerText;
	private TextView questionText;
	private TextView letterHelp;

	private Button sendButton;
	private Button getLetterButton;

	private RecyclerView answersRecyclerView;
	private AnswerAdapter answerAdapter;
	private RecyclerView.LayoutManager mLayoutManager;
	private List<PlayerAnswer> answersList = new ArrayList<>();

	private RecyclerView userRecyclerView;
	private PlayerAdapter playerAdapter;
	private List<UserDto> userList = new ArrayList<>();

	private SpeechRecognizerManager mSpeechManager;

	private QuestionHandler questionHandler;

	private GameController gameController;


	public static boolean populated;


	public void sendAnswer(String answer){
		PlayerAnswer playerAnswer = new PlayerAnswer();
		playerAnswer.setDescription(answer);
		playerAnswer.setQuestionId(this.currentQuestionId);
		gameController.sendAnswer(LocalCache.getInstance().getString(CURRENT_GAME_ID), playerAnswer);
	}

	public  void showAnswer(PlayerAnswer playerAnswer){
				runOnUiThread(() -> {
					setAnswer(playerAnswer);
	             });
	}

	void setAnswer(PlayerAnswer answer){
			Boolean isCorrect = answer.isCorrect();
			Boolean hasAlreadyBeenSaid = answer.isHasAlreadyBeenSaid();
			String player = answer.getPlayer().getUsername();
			String description = answer.getDescription();
			if(isCorrect){
				answerText.setText("");

						answersList.add(answer);
						//answerAdapter.notifyItemInserted(answersList.size() - 1);
						answersRecyclerView.smoothScrollToPosition(answersList.size() - 1);
						answerAdapter.notifyDataSetChanged();

						updatePlayerScore(player, answer.getPoints().toString());
			}else{

				if(hasAlreadyBeenSaid){
					Toast.makeText(getBaseContext(), "hasAlreadyBeenSaid: " + player + ": " + description, Toast.LENGTH_SHORT).show();
				}else{
					answerText.setText("");
					answersList.add(answer);
//					answerAdapter.notifyItemInserted(answersList.size() - 1);
					answersRecyclerView.smoothScrollToPosition(answersList.size() - 1);
					answerAdapter.notifyDataSetChanged();
				}
			}
    }

	public  void setGameState(GameState gamestate){
		runOnUiThread(() -> {


				Log.i(Const.TAG, "GameState：" + gamestate);
				setPlayerList(gamestate.getPlayers());
				dateStarted = gamestate.getDateStarted().getTime();
				dateCurrent = gamestate.getCurrentDate().getTime();

				final Handler someHandler = new Handler(getMainLooper());

				someHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						Date date = new Date(dateCurrent);
						date.setTime(date.getTime() + 1000);
						dateCurrent = date.getTime();

						someHandler.postDelayed(this, 1000);
					}
				}, 10);

				setQuestionText(gamestate.getQuestions(), dateStarted , dateCurrent);
		});
	}
	ExplosionField explosionField;

	void initComponents(){
		getSupportActionBar().hide();
		explosionField = ExplosionField.attach2Window(this);
		questionHandler = new QuestionHandlerImpl();

		answerText = findViewById(R.id.editText);
		questionText = findViewById(R.id.textView);
		letterHelp = findViewById(R.id.letterHelp);
		sendButton = findViewById(R.id.send);
		getLetterButton = findViewById(R.id.getLetter);
		mProgressBar=(ProgressBar)findViewById(R.id.progressBar);


        userRecyclerView = findViewById(R.id.my_recycler_viewH);
      //  userRecyclerView.addItemDecoration(new DividerItemDecoration(GameActivity.this, LinearLayoutManager.HORIZONTAL));
        playerAdapter = new PlayerAdapter(userList, getApplicationContext());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(GameActivity.this, LinearLayoutManager.HORIZONTAL, false);
        userRecyclerView.setLayoutManager(horizontalLayoutManager);
        userRecyclerView.setAdapter(playerAdapter);


        answersRecyclerView = findViewById(R.id.my_recycler_view);
        answersRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        answersRecyclerView.setLayoutManager(mLayoutManager);


		answerText.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start,
										  int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start,
									  int before, int count) {
				if(s != null && s.length() > 0){
					PlayerAnswer playerAnswer = new PlayerAnswer();
					playerAnswer.setDescription(s.toString());
					//playerAnswer.setQuestionId(1);
					playerAnswer.setQuestionId(currentQuestionId);
					Answer answer = questionHandler.isAnswerValid(playerAnswer);
					if(answer != null){
						//answerText.setText(answer.getDescription());
						sendAnswer(s.toString());
						Toast.makeText(MenuActivity.getAppContext(), answer.getDescription(), Toast.LENGTH_SHORT).show();
					}
				}

			}
		});


//		viewKonfetti = (KonfettiView)findViewById(R.id.viewKonfetti);
////		viewKonfetti.build()
////				.addColors(Color.RED, Color.GREEN)
////				.setSpeed(1f, 5f)
////				.setFadeOutEnabled(true)
////				.setTimeToLive(2000L)
////				.addShapes(Shape.RECT, Shape.CIRCLE)
////				.setPosition(0f, -359f, -359f, 0f)
////				.stream(200, 5000L);
//
//		final KonfettiView konfettiView = (KonfettiView)findViewById(R.id.viewKonfetti);
//		konfettiView.build()
//				.addColors(Color.RED, Color.GREEN)
//				.setSpeed(1f, 5f)
//				.setFadeOutEnabled(true)
//				.setTimeToLive(2000L)
//				.addShapes(Shape.RECT, Shape.CIRCLE)
//				.setPosition(0f, -359f, -359f, 0f)
//				.stream(200, 5000L);


		sendButton.setOnClickListener(v -> {

			//explosionField.explode(v);
			//Toast.makeText(MenuActivity.getAppContext(), getAmplitude()+"", Toast.LENGTH_SHORT).show();
			//startSpeak();

			sendAnswer(answerText.getText().toString());

//			Alerter.create(this)
//					.setTitle("Ετοιμαστείτε για το γύρο 2!")
////					.setIcon(R.drawable.alerter_ic_mail_outline)
////					.setText("Alert text...")
//					.setDuration(5000)
//					//.hideIcon()
//					.enableVibration(true)
//					.setBackgroundColorRes(R.color.colorAccent) // or setBackgroundColorInt(Color.CYAN)
//					.show();


		});

		getLetterButton.setOnClickListener(v -> {
			letterHelp.setVisibility(View.VISIBLE);
		});

    }


	private void updatePlayerScore(String username, String points) {
		for (UserDto player :userList){
			if(player.getUsername().equals(username)){
				player.getPlayerDetails().setElo(player.getPlayerDetails().getElo() + Integer.valueOf(points));
			}
		}
		playerAdapter.notifyDataSetChanged();
	}

	void setPlayerList(List<UserDto> players){
		userList.clear();
		userList.addAll(players);
		playerAdapter.notifyDataSetChanged();
	}

	void setQuestionText(List<Question> questions, Long dateStarted, Long currentDate)  {
		questionHandler.setQuestions(questions);
		move(questionHandler.getQuestions(), dateStarted, currentDate, true);
	}



//	public void updateTime(JSONObject currentTime) throws JSONException {
//		runOnUiThread(() -> {
//
//		if(mCountDownTimer != null){
//			mCountDownTimer.cancel();
//			mCountDownTimer = null;
//		}
//
//		timerStep = 0;
//
//				move(questionHandler.getQuestions(),  dateStarted, dateCurrent, false);
//
//
//		});
//	}
	void move(List<Question> questions, Long dateStarted, Long currentDate, boolean setQuestion){
		startDate = new Date(dateStarted);
		currentDat = new Date(currentDate);

		long diff = currentDat.getTime() - startDate.getTime();//as given
		int secondsRemain;
		int minutes;
		int secondsPass = 0;
		if(diff == 0){
			secondsRemain = 1000 * 60 ;
			minutes = 0;
		}else{
			minutes = (int)TimeUnit.MILLISECONDS.toMinutes(diff);
			secondsPass = (int)TimeUnit.MILLISECONDS.toSeconds(diff) - minutes *60;
			secondsRemain = (60 - secondsPass) * 1000;
		}


		boolean duringChangeRound = secondsPass < 5;
		if(duringChangeRound){
			int secondsRemainToChangeRound = 5 - secondsPass;
			if((minutes+1) >1) {
				mProgressBar.setProgress(100);
			}
			timerStep=0;

			answerText.setText("");
//			Alerter.create(GameActivity.instance)
//					.setTitle("Ετοιμαστείτε για το γύρο " + (minutes+1)+"!")
////					.setIcon(R.drawable.alerter_ic_mail_outline)
////					.setText("Alert text...")
//					.setDuration(secondsRemainToChangeRound*1000)
//					//.hideIcon()
//					.enableVibration(true)
//					.setBackgroundColorRes(R.color.colorAccent) // or setBackgroundColorInt(Color.CYAN)
//					.show();

			new Handler()
					.postDelayed(()->
									moveToNextQuestion(questions,
											minutes,
											0,
											55 * 1000 ,
											true),
							secondsRemainToChangeRound*1000);
		}
		else{
			moveToNextQuestion(questions, minutes, secondsPass-5, secondsRemain+5, setQuestion);
		}
	}

	void moveToNextQuestion(List<Question> questions, int currentQuestionIndex, int seconds, int remains, boolean setQuestion){

	    if(currentQuestionIndex == questions.size()){

	        return;
        }
		mCountDownTimer = new CountDownTimer(remains,1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				//Log.v("Log_tag", "Tick of Progress"+ timerStep+ millisUntilFinished);

				int step = ++timerStep * (100  - (seconds*100/60));
				mProgressBar.setProgress((seconds*100/60) + step/(remains/1000));

			}

			@Override
			public void onFinish() {
				mProgressBar.setProgress(100);
				timerStep=0;
				answerText.setText("");
//				Alerter.create(GameActivity.instance)
//						.setTitle("Ετοιμαστείτε για το γύρο "+ (currentQuestionIndex+2) +"!")
////					.setIcon(R.drawable.alerter_ic_mail_outline)
////					.setText("Alert text...")
//						.setDuration(4500)
//						//.hideIcon()
//						.enableVibration(true)
//						.setBackgroundColorRes(R.color.colorAccent) // or setBackgroundColorInt(Color.CYAN)
//						.show();

				new Handler()
						.postDelayed(()->
								moveToNextQuestion(questions,
										currentQuestionIndex + 1,
										0,
										55 * 1000 ,
										true),
								        5000);
			}
		};
		mCountDownTimer.start();


		if(setQuestion){
			setCurrentQuestion(questions, currentQuestionIndex);
		}

	}

	void setCurrentQuestion(List<Question> questions, int currentQuestionIndex){
			//setAnswers( new JSONArray());

			this.currentQuestionId = questions.get(currentQuestionIndex).getId();
			setQuestionText(questions.get(currentQuestionIndex).getDescription());

		answersList.clear();
		answerAdapter = new com.katiforis.top10.adapter.AnswerAdapter(answersList);
		answerAdapter.notifyDataSetChanged();
		answersRecyclerView.setAdapter(answerAdapter);
		for(PlayerAnswer playerAnswer :questions.get(currentQuestionIndex).getCurrentAnswers()){
			setAnswer(playerAnswer);
		}
	}

	void setQuestionText(String question){
		Animation a = AnimationUtils.loadAnimation(this, R.anim.text_animation);
		a.reset();
		questionText.setText(question);
		questionText.clearAnimation();
		questionText.startAnimation(a);
	}

//	@Override
//	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//		switch(requestCode)
//		{
//			case PermissionHandler.RECORD_AUDIO:
//				if(grantResults.length>0) {
//					if(grantResults[0]== PackageManager.PERMISSION_GRANTED) {
//						startSpeak();
//					}
//				}
//				break;
//
//		}
//	}
//
//	private void SetSpeechListener()
//	{
//		mSpeechManager=new SpeechRecognizerManager(this, new SpeechRecognizerManager.onResultsReady() {
//			@Override
//			public void onResults(ArrayList<String> results) {
//
//				if(results!=null && results.size()>0)
//				{
////
////					if(results.size()==1)
////					{
////						//mSpeechManager.destroy();
////						//mSpeechManager = null;
////						result_tv.setText(results.get(0));
////					}
////					else {
//						StringBuilder sb = new StringBuilder();
////						if (results.size() > 5) {
////							results = (ArrayList<String>) results.subList(0, 5);
////						}
//						for (String result : results) {
//							sb.append(result).append("|");
//						}
////						result_tv.setText(sb.toString());
////					}
//
//					//Toast.makeText(MenuActivity.getAppContext(), sb.toString(), Toast.LENGTH_SHORT).show();
//					sendAnswer(sb.toString());
//				}
//				else{
//				//	Toast.makeText(MenuActivity.getAppContext(), "no results", Toast.LENGTH_SHORT).show();
//				}
//			}
//		});
//	}

//	void startSpeak(){
//		if(PermissionHandler.checkPermission(this,PermissionHandler.RECORD_AUDIO)) {
//
//			if (mSpeechManager == null) {
//				SetSpeechListener();
//			} else if (!mSpeechManager.ismIsListening()) {
//				mSpeechManager.destroy();
//				SetSpeechListener();
//			}
//		}else
//		{
//			PermissionHandler.askForPermission(PermissionHandler.RECORD_AUDIO,this);
//		}
//	}

	@SuppressLint("CheckResult")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_layout);
		initComponents();
		INSTANCE = this;
		populated = false;
		gameController = GameController.getInstance();
		gameController.setGameActivity(this);
		answerText.requestFocus();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(mCountDownTimer != null){
			mCountDownTimer.cancel();
			mCountDownTimer = null;
			timerStep=0;
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		String gameId = LocalCache.getInstance().getString(CURRENT_GAME_ID);
		if (gameId == null || gameId.length() == 0) {
			return;
		}
		gameController.getGameState(gameId);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
}
