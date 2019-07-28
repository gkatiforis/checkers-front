package com.katiforis.checkers.DTO.request;

public class Reward extends BaseRequest {
	private String type;
	private int amount;

	public Reward() {
		super();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
}
