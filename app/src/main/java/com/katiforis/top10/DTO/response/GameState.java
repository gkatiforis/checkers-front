package com.katiforis.top10.DTO.response;

import com.katiforis.top10.DTO.UserDto;
import com.katiforis.top10.DTO.Question;

import java.util.Date;
import java.util.List;

public class GameState extends GameResponse {
    private List<UserDto> players;
    private List<Question> questions;
    private Date dateStarted;
    private Date currentDate;

    public GameState(){
        super();
    }
    public List<UserDto> getPlayers() {
        return players;
    }

    public void setPlayers(List<UserDto> players) {
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
