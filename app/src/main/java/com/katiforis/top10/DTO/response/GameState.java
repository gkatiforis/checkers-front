package com.katiforis.top10.DTO.response;

import com.katiforis.top10.DTO.PlayerDto;
import com.katiforis.top10.DTO.Question;

import java.util.Date;
import java.util.List;

public class GameState extends GameResponse {
    private List<PlayerDto> players;
    private List<Question> questions;
    private Date dateStarted;
    private Date currentDate;

    public GameState(){
        super();
    }
    public List<PlayerDto> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerDto> players) {
        this.players = players;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Date getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(Date dateStarted) {
        this.dateStarted = dateStarted;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }
}
