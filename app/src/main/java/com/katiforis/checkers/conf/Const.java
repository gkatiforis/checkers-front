package com.katiforis.checkers.conf;

public class Const {
	public static final String host_stomp = "ws://192.168.1.5:5000/top/websocket";
//	public static final String host_stomp = "ws://192.168.16.116:5000/top/websocket";
	public static final String android_web_id = "794218231402-hb2lq8lk48qf8tb755ha9dugiuvkfj3r.apps.googleusercontent.com";
	//public static final String address = "ws://checkers.us-east-2.elasticbeanstalk.com/im/websocket";
	public static final String placeholder = "placeholder";

	public static final String MENU = "/menu";
	public static final String FIND_GAME = MENU + "/game/find";
	public static final String GET_FRIEND_LIST = MENU + "/friend";
	public static final String GET_NOTIFICATION_LIST = MENU + "/notification";
	public static final String GET_RANK = MENU + "/rank";
	public static final String GET_USER_DETAILS = MENU + "/details";
	public static final String GET_LOBBY = MENU + "/lobby";
	public static final String MAIN_TOPIC_RESPONSE = "/user/main";

	public static final String SEND_WORD = "/game/group/game/" + placeholder;
	public static final String GET_GAME_STATE = "/game/gamestate";
	public static final String GAME_RESPONSE = "/game/group/" + placeholder;

	public static final String TAG = "TOP10";

}
