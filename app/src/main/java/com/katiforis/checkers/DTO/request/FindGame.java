package com.katiforis.checkers.DTO.request;

public class FindGame extends BaseRequest {
	private String gameId;
	private boolean restart;

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
}
