package com.katiforis.top10.DTO.request;

public class FindGame extends BaseRequest {
	private String gameId;

	public FindGame() {
		super();
	}

	public FindGame(String gameId) {
		this.gameId = gameId;
	}

	public FindGame(String playerId, String gameId) {
		super(playerId);
		this.gameId = gameId;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
}
