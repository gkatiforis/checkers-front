package com.katiforis.checkers.DTO.response;

public class OfferDraw extends GameResponse {
        String byUser;
        public OfferDraw(String gameId) {
        super(ResponseState.OFFER_DRAW.getState(), gameId);
    }
    public OfferDraw() {
        super();
    }
    public String getByUser() {
        return byUser;
    }

    public void setByUser(String byUser) {
        this.byUser = byUser;
    }
}
