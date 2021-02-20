package com.example.pfamonument.Model;

/**
 * Created by SIS on 08/03/2019.
 */

public class Question {
    public String question;
    public String option1;
    public String option2;
    public String option3;
    public String reponse;
    public int questionId;

    public Question(String question, String option1, String option2, String option3, String reponse, int questionId) {
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.reponse = reponse;
        this.questionId = questionId;
    }

    public Question () { }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }
}
