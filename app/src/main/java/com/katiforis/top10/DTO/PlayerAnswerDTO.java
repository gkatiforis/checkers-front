package com.katiforis.top10.DTO;

public class PlayerAnswerDTO {
    private String answer;
   private String userId;
   private long questionId;

    public void setAnswer(String name) {
        this.answer = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAnswer() {
        return answer;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }
}
