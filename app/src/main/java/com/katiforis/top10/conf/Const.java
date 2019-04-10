package com.katiforis.top10.conf;

public class Const {
	public static final String address = "ws://192.168.16.116:5000/im/websocket";

	//public static final String address = "ws://top10.us-east-2.elasticbeanstalk.com/im/websocket";

	public static final String TAG = "TOP10";
	public static final String placeholder = "placeholder";
	public static final String groupWord = "/game/group/word/" + placeholder;
	public static final String groupResponse = "/g/" + placeholder;
	public static final String chat = "/game/chat/find";
	public static final String login = "/player/login";
	public static final String gameState = "/game/chat/gamestate";
	public static final String chatResponse = "/user/" + placeholder + "/msg";
}
