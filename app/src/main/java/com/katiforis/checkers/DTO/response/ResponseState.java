package com.katiforis.checkers.DTO.response;

public enum ResponseState {

    GAME_STATE("game_state"),
    ANSWER("answer"),
    END_GAME("end_game"),
    START_GAME("start_game"),
    FRIEND_LIST("friend_list"),
    LOBBY("lobby"),
    NOTIFICATION_LIST("notification_list"),
    RANK_LIST("rank_list"),
    USER_STATS("user_stats");

    public String state;

    ResponseState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
