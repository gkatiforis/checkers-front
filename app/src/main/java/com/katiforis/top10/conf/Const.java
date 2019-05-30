package com.katiforis.top10.conf;

public class Const {
	public static final String host_stomp = "ws://192.168.1.5:5000/top/websocket";
	public static final String android_web_id = "777150420367-5db6749a0pl5cfbpshf3cgb8g94g448f.apps.googleusercontent.com";
	//public static final String address = "ws://top10.us-east-2.elasticbeanstalk.com/im/websocket";
	public static final String placeholder = "placeholder";

	public static final String MENU = "/menu";
	public static final String FIND_GAME = MENU + "/game/find";
	public static final String LOGIN = MENU + "/login";
	public static final String GET_FRIEND_LIST = MENU + "/friend";
	public static final String GET_NOTIFICATION_LIST = MENU + "/notification";
	public static final String GET_RANK = MENU + "/rank";
	public static final String GET_USER_DETAILS = MENU + "/details";
	public static final String GET_LOBBY = MENU + "/lobby";
	public static final String MAIN_TOPIC_RESPONSE = "/user/main";

	public static final String SEND_WORD = "/game/group/word/" + placeholder;
	public static final String GET_GAME_STATE = "/game/gamestate";
	public static final String GAME_RESPONSE = "/game/group/" + placeholder;

	public static final String TAG = "TOP10";

}
