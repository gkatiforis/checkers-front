package com.katiforis.top10.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.katiforis.top10.DTO.AnswerDTO;
import com.katiforis.top10.DTO.GamePlayerDTO;
import com.katiforis.top10.DTO.PlayerAnswerDTO;
import com.katiforis.top10.DTO.QuestionDTO;
import com.katiforis.top10.R;
import com.katiforis.top10.conf.Const;
import com.katiforis.top10.game.QuestionHandler;
import com.katiforis.top10.game.QuestionHandlerImpl;
import com.katiforis.top10.speech.PermissionHandler;
import com.katiforis.top10.speech.SpeechRecognizerManager;
import com.katiforis.top10.stomp.MyStomp;
import com.katiforis.top10.adapter.AnswerItem;
import com.katiforis.top10.adapter.UserAdapter;

import tyrantgit.explosionfield.ExplosionField;

public class GameActivity extends Activity {

    public static GameActivity instance;
//	public static String gameId;
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
	private com.katiforis.top10.adapter.AnswerAdapter answerAdapter;
	private RecyclerView.LayoutManager mLayoutManager;
	private List<AnswerItem> answersList = new ArrayList<>();

	private RecyclerView userRecyclerView;
	private UserAdapter userAdapter;
	private List<GamePlayerDTO> userList = new ArrayList<>();

	private SpeechRecognizerManager mSpeechManager;

	private QuestionHandler questionHandler;

	public void sendAnswer(String answer){
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("answer", answer);
			jsonObject.put("userId", MenuActivity.userId);
			jsonObject.put("questionId",  this.currentQuestionId);
			jsonObject.put("status",  "answer");

		} catch (JSONException e) {
			System.out.print("error: " + e);
		}

		MyStomp.sendToGroup(MenuActivity.getGameId(), jsonObject);
	}


	public  void showAnswer(JSONObject jsonObject){
				runOnUiThread(() -> {

					setAnswer(jsonObject);
				//	startSpeak();

	             });
	}



	void setAnswer(JSONObject jsonObject){
        try {

			// Toast.makeText(getBaseContext(), (jsonObject.toString()), Toast.LENGTH_SHORT).show();
			Boolean isCorrect = jsonObject.getBoolean("correct");
			Boolean hasAlreadyBeenSaid = jsonObject.getBoolean("hasAlreadyBeenSaid");
			String player = jsonObject.getJSONObject("player").get("username").toString();
			JSONObject answer = jsonObject.getJSONObject("answer");
			long answerId = answer.getLong("id");
			String description = answer.get("description").toString();
			if(isCorrect){
				answerText.setText("");

				QuestionDTO questionDTO =questionHandler.getQuestionById(currentQuestionId);
				List<AnswerDTO> answerDTOS = questionDTO.getAnswers();
				for(AnswerDTO answerDTO:answerDTOS){
					if(answerId == answerDTO.getId()){
						String points = answer.get("points").toString();

						AnswerItem answerItem = new AnswerItem(answerId, description, player, points);

						answersList.add(answerItem);
						//answerAdapter.notifyItemInserted(answersList.size() - 1);
						answersRecyclerView.smoothScrollToPosition(answersList.size() - 1);
						answerAdapter.notifyDataSetChanged();

						updatePlayerScore(player, points);
					}
				}

			}else{

				if(hasAlreadyBeenSaid){
					Toast.makeText(getBaseContext(), "hasAlreadyBeenSaid: " + player + ": " + description, Toast.LENGTH_SHORT).show();
				}else{
					answerText.setText("");
					AnswerItem answerItem = new AnswerItem(answerId, description, player, "0");
					answersList.add(answerItem);
//					answerAdapter.notifyItemInserted(answersList.size() - 1);
					answersRecyclerView.smoothScrollToPosition(answersList.size() - 1);
					answerAdapter.notifyDataSetChanged();
				}
			}

			//show.append(jsonObject.getString("response") + "\n");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


	public  void setGameState(JSONObject gamestate){
		runOnUiThread(() -> {
			try {

				Log.i(Const.TAG, "GameState：" + gamestate);
				setPlayerList(gamestate.getJSONArray("players"));
				dateStarted = gamestate.getLong("dateStarted");
				dateCurrent = gamestate.getLong("currentDate");

//				final Handler someHandler = new Handler(getMainLooper());
//
//				someHandler.postDelayed(new Runnable() {
//					@Override
//					public void run() {
////				tvClock.setText(new SimpleDateFormat("HH:mm", Locale.US).format(new Date()));
//
//						Date date = new Date(dateCurrent);
//						date.setTime(date.getTime() + 1000);
//						dateCurrent = date.getTime();
//
//						someHandler.postDelayed(this, 1000);
//					}
//				}, 10);

				setQuestionText(gamestate.getJSONArray("questions"), dateStarted , dateCurrent);
				setAnswers(gamestate.getJSONArray("currentAnswers"));

			} catch (JSONException e) {

				e.printStackTrace();
			}catch (Exception e){
				e.printStackTrace();
				Log.i(Const.TAG, "error：" + e);
			}
		});
	}
	ExplosionField explosionField;



	void initComponents(){
		explosionField = ExplosionField.attach2Window(this);
		questionHandler = new QuestionHandlerImpl();
		//questionHandler.initTest();

		answerText = findViewById(R.id.editText);
		questionText = findViewById(R.id.textView);
		letterHelp = findViewById(R.id.letterHelp);
		sendButton = findViewById(R.id.send);
		getLetterButton = findViewById(R.id.getLetter);
		mProgressBar=(ProgressBar)findViewById(R.id.progressBar);


        userRecyclerView = findViewById(R.id.my_recycler_viewH);
      //  userRecyclerView.addItemDecoration(new DividerItemDecoration(GameActivity.this, LinearLayoutManager.HORIZONTAL));
        userAdapter = new UserAdapter(userList, getApplicationContext());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(GameActivity.this, LinearLayoutManager.HORIZONTAL, false);
        userRecyclerView.setLayoutManager(horizontalLayoutManager);
        userRecyclerView.setAdapter(userAdapter);


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
					PlayerAnswerDTO playerAnswerDTO = new PlayerAnswerDTO();
					playerAnswerDTO.setAnswer(s.toString());
					//playerAnswerDTO.setQuestionId(1);
					playerAnswerDTO.setQuestionId(currentQuestionId);
					playerAnswerDTO.setUserId(MenuActivity.userId);
					AnswerDTO answerDTO = questionHandler.isAnswerValid(playerAnswerDTO);
					if(answerDTO != null){
						//answerText.setText(answerDTO.getDescription());
						sendAnswer(s.toString());
						Toast.makeText(MenuActivity.getAppContext(), answerDTO.getDescription(), Toast.LENGTH_SHORT).show();
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
		for (GamePlayerDTO gamePlayerDTO:userList){
			if(gamePlayerDTO.getUsername().equals(username)){
				gamePlayerDTO.setPoints(gamePlayerDTO.getPoints() + Integer.valueOf(points));
			}
		}
		userAdapter.notifyDataSetChanged();
	}

	void setPlayerList(JSONArray players){

		userList.clear();

		try {
			for (int i = 0; i < players.length(); i++){
				String playerId = players.getJSONObject(i).getString("playerId");
				String username = players.getJSONObject(i).getString("username");
				Integer points = players.getJSONObject(i).getInt("points");
				userList.add(new GamePlayerDTO(playerId, username, points));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}


		userAdapter.notifyDataSetChanged();
	}



	void setQuestionText(JSONArray questions, Long dateStarted, Long currentDate) throws JSONException {


		List<QuestionDTO> questionDTOS = new ArrayList<>();

		for(int i = 0; i < questions.length(); i++){
			JSONObject	question = questions.getJSONObject(i);

			List<AnswerDTO> answerDTOS  = new ArrayList<>();
			JSONArray answers = question.getJSONArray("answers");
			for(int j = 0; j < answers.length(); j++){
				JSONObject answer =	answers.getJSONObject(j);
				answerDTOS.add(new AnswerDTO(answer.getLong("id"), answer.getString("description"),
						answer.getInt("points")));
			}
			questionDTOS.add(new QuestionDTO(question.getLong("id"), question.getString("description"), answerDTOS));
		}

		questionHandler.setQuestions(questionDTOS);

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
	void move(List<QuestionDTO> questions, Long dateStarted, Long currentDate, boolean setQuestion){
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

	void moveToNextQuestion(List<QuestionDTO> questions, int currentQuestionIndex, int seconds, int remains, boolean setQuestion){

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

	void setCurrentQuestion(List<QuestionDTO> questions, int currentQuestionIndex){
			setAnswers( new JSONArray());
			this.currentQuestionId = questions.get(currentQuestionIndex).getId();
			setQuestionText(questions.get(currentQuestionIndex).getDescription());
	}

	void setQuestionText(String question){
		Animation a = AnimationUtils.loadAnimation(this, R.anim.text_animation);
		a.reset();
		questionText.setText(question);
		questionText.clearAnimation();
		questionText.startAnimation(a);
	}


	void setAnswers(JSONArray answers){

        answersList.clear();

		try {
			for (int i = 0; i < answers.length(); i++){
                JSONObject answer = answers.getJSONObject(i);
                long questionId = answer.getJSONObject("question").getLong("id");
                if(questionId == currentQuestionId){
					setAnswer(answer);
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}


		answerAdapter = new com.katiforis.top10.adapter.AnswerAdapter(answersList);
		answerAdapter.notifyDataSetChanged();
		answersRecyclerView.setAdapter(answerAdapter);
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
		setContentView(R.layout.activity_game);
		this.init();
		answerText.requestFocus();
	}

	private void init() {
		initComponents();
		instance = this;
		MyStomp.initConnGroup(MenuActivity.getGameId());





	}

	@Override
	protected void onPause() {
		super.onPause();


	//	MyStomp.dis();
		if(mCountDownTimer != null){
			mCountDownTimer.cancel();
			mCountDownTimer = null;
			timerStep=0;
		}

	}

	@Override
	protected void onStart() {
		super.onStart();
		PermissionHandler.askForPermission(PermissionHandler.RECORD_AUDIO,this);

		//	start();

		//	if(!PermissionHandler.checkPermission(this,PermissionHandler.RECORD_AUDIO)) {
		//PermissionHandler.askForPermission(PermissionHandler.RECORD_AUDIO,this);
		//	}

		String gameId = MenuActivity.getGameId();
		if (gameId.length() == 0) {
			return;
		}

		MyStomp.sendGetGameState( MenuActivity.userId.trim(), gameId.trim());




		//	setGameState();

	}

	@Override
	protected void onStop() {
		super.onStop();


		//		if(mSpeechManager!=null) {
//			mSpeechManager.destroy();
//			mSpeechManager=null;
//		}

	}


}
