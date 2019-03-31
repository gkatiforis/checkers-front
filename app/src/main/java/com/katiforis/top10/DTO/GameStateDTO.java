package com.katiforis.top10.DTO;

import java.util.Date;
import java.util.List;

public class GameStateDTO extends GameDTO {
    private List<GamePlayerDTO> players;
    private List<QuestionDTO> questions;
    private Date dateStarted;
    private Date currentDate;

    public GameStateDTO(){
        super("");
    }
    public List<GamePlayerDTO> getPlayers() {
        return players;
    }

    public void setPlayers(List<GamePlayerDTO> players) {
        this.players = players;
    }

    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDTO> questions) {
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
