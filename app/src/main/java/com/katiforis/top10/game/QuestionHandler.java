package com.katiforis.top10.game;

import com.katiforis.top10.DTO.Answer;
import com.katiforis.top10.DTO.PlayerAnswer;
import com.katiforis.top10.DTO.Question;

import java.util.List;

public interface QuestionHandler {
    void setQuestions(List<Question> questions);
    List<Question> getQuestions();
    Answer isAnswerValid(PlayerAnswer playerAnswer);
    Question getQuestionById(Long id);
}

