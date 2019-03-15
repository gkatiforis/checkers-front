package com.katiforis.top10.DTO;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class QuestionDTO implements Serializable {

    private long id;

    private String description;

    private List<AnswerDTO> answers;

    public QuestionDTO(String description, List<AnswerDTO> answers) {
        this.description = description;
        this.answers = answers;
    }

    public QuestionDTO(long id, String description, List<AnswerDTO> answers) {
        this.id = id;
        this.description = description;
        this.answers = answers;
    }

    public QuestionDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public List<AnswerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerDTO> answers) {
        this.answers = answers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuestionDTO that = (QuestionDTO) o;

        if (id != that.id) return false;
        if (!description.equals(that.description)) return false;
        return answers.equals(that.answers);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + description.hashCode();
        result = 31 * result + answers.hashCode();
        return result;
    }
}
