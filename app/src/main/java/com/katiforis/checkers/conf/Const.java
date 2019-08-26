package com.katiforis.checkers.conf;

public class Const {
//	public static final String host_stomp = "ws://checkers.eu-west-2.elasticbeanstalk.com/top/websocket";
	public static final String host_stomp = "ws://192.168.1.2:5000/top/websocket";
//	public static final String host_stomp = "ws://192.168.16.116:5000/top/websocket";
	public static final String android_web_id = "794218231402-hb2lq8lk48qf8tb755ha9dugiuvkfj3r.apps.googleusercontent.com";
	//794218231402-kudgnejuv5agonchn70q4o5nbth2llmq.apps.googleusercontent.com
	//public static final String address = "ws://checkers.us-east-2.elasticbeanstalk.com/im/websocket";

//	Ad format	Sample ad unit ID
//	Banner	ca-app-pub-3940256099942544/6300978111
//	Interstitial	ca-app-pub-3940256099942544/1033173712
//	Interstitial Video	ca-app-pub-3940256099942544/8691691433
//	Rewarded Video	ca-app-pub-3940256099942544/5224354917
//	Native Advanced	ca-app-pub-3940256099942544/2247696110
//	Native Advanced Video	ca-app-pub-3940256099942544/1044960115
	public static String APP_AD_ID = "ca-app-pub-4642562250660220~3506267101";
	public static String VIEW_AD_ID = "ca-app-pub-3940256099942544/6300978111";
	public static String INTERSTITIAL_AD_ID = "ca-app-pub-3940256099942544/1033173712";
	public static String REWARD_AD_ID = "ca-app-pub-3940256099942544/5224354917";

//	public static String APP_AD_ID = "ca-app-pub-4642562250660220~3506267101";
//	public static String VIEW_AD_ID = "ca-app-pub-4642562250660220/6870797040";
//	public static String INTERSTITIAL_AD_ID = "ca-app-pub-4642562250660220/3032528343";
//	public static String REWARD_AD_ID = "ca-app-pub-4642562250660220/9953159912";

	public static final String placeholder = "placeholder";

	public static final String MENU = "/menu";
	public static final String KEEP_CONNECTION = MENU + "/keepConnection";
	public static final String FIND_GAME = MENU + "/game/find";
	public static final String GET_FRIEND_LIST = MENU + "/friend";
	public static final String GET_NOTIFICATION_LIST = MENU + "/notification";
	public static final String GET_RANK = MENU + "/rank";
	public static final String SEND_REWARD = MENU + "/reward";
	public static final String GET_USER_DETAILS = MENU + "/details";
	public static final String GET_LOBBY = MENU + "/lobby";
	public static final String MAIN_TOPIC_RESPONSE = "/user/main";

	public static final String SEND_WORD = "/game/group/game/" + placeholder;
	public static final String GET_GAME_STATE = "/game/gamestate";
	public static final String GAME_RESPONSE = "/game/group/" + placeholder;

	public static final String TAG = "checkers";

	public final static int OFFER_DRAW_TIME_IN_SECONDS = 30;

}
