package com.katiforis.top10.game;

import com.katiforis.top10.DTO.AnswerDTO;
import com.katiforis.top10.DTO.PlayerAnswerDTO;
import com.katiforis.top10.DTO.QuestionDTO;

import java.util.List;

public interface QuestionHandler {
//    void initTest();
    void setQuestions(List<QuestionDTO> questions);
    List<QuestionDTO> getQuestions();
    AnswerDTO isAnswerValid(PlayerAnswerDTO playerAnswerDTO);
    QuestionDTO getQuestionById(Long id);
}

