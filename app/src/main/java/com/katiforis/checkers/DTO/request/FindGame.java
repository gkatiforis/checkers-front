package com.katiforis.checkers.DTO.request;

import com.katiforis.checkers.DTO.GameType;

public class FindGame extends BaseRequest {
	private String gameId;
	private boolean restart;
	private GameType gameType;

	public FindGame() {
		super();
	}

	public FindGame(String gameId) {
		this.gameId = gameId;
	}


	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public boolean isRestart() {
		return restart;
	}

	public void setRestart(boolean restart) {
		this.restart = restart;
	}

	public GameType getGameType() {
		return gameType;
	}

	public void setGameType(GameType gameType) {
		this.gameType = gameType;
	}
}
