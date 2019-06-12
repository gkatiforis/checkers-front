package com.katiforis.checkers.DTO;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class Question implements Serializable {

    private long id;
    private String description;
    private List<Answer> answers;
    private Set<PlayerAnswer> currentAnswers;

    public Question(String description, List<Answer> answers, Set<PlayerAnswer> currentAnswers) {
        this.description = description;
        this.answers = answers;
        this.currentAnswers = currentAnswers;
    }
    public Question(String description, List<Answer> answers) {
        this.description = description;
        this.answers = answers;
    }

    public Question(long id, String description, List<Answer> answers) {
        this.id = id;
        this.description = description;
        this.answers = answers;
    }
    public Question(long id, String description, List<Answer> answers, Set<PlayerAnswer> currentAnswers) {
        this.id = id;
        this.description = description;
        this.answers = answers;
        this.currentAnswers = currentAnswers;
    }
    public Question() {
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


    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public Set<PlayerAnswer> getCurrentAnswers() {
        return currentAnswers;
    }

    public void setCurrentAnswers(Set<PlayerAnswer> currentAnswers) {
        this.currentAnswers = currentAnswers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question that = (Question) o;

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
