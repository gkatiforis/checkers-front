package com.katiforis.checkers.DTO.request;

public class FindGame extends BaseRequest {
	private String gameId;

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
}
