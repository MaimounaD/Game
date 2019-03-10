package com.example.game;

public class QuestionQuizz {
    private String question;
    private String choice1, choice2, choice3, choice4;
    private String answer;


    public QuestionQuizz() {

    }

    public QuestionQuizz(String q, String c1, String c2, String c3, String a) {
        this.question = q;
        this.choice1 = c1;
        this.choice2 = c2;
        this.choice3 = c3;
        this.answer = a;
    }

    public QuestionQuizz(String q, String c1, String c2, String c3, String c4, String a) {
        this.question = q;
        this.choice1 = c1;
        this.choice2 = c2;
        this.choice3 = c3;
        this.choice4 = c4;
        this.answer = a;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getChoice1() {
        return choice1;
    }

    public void setChoice1(String choice1) {
        this.choice1 = choice1;
    }

    public String getChoice2() {
        return choice2;
    }

    public void setChoice2(String choice2) {
        this.choice2 = choice2;
    }

    public String getChoice3() {
        return choice3;
    }

    public void setChoice3(String choice3) {
        this.choice3 = choice3;
    }
    public String getChoice4() {
        return choice4;
    }

    public void setChoice4(String choice4) {
        this.choice4 = choice4;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
