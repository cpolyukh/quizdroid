package edu.washington.cpolyukh.quizdroid;

/**
 * Created by christina3135 on 5/6/2015.
 */
class Question {
    private String question;
    private String[] answers;
    private int correctAnswerNum; //NOTE: NOT the index (values 1-4)

    public Question(String question, String answer1, String answer2,
                    String answer3, String answer4, int correctAnswerNum) {
        this.question = question;
        this.answers = new String[]{answer1, answer2, answer3, answer4};
        this.correctAnswerNum = correctAnswerNum;
    }

    public Question(String question, String[] answers, int correctAnswerNum) {
        this.question = question;
        this.answers = answers;
        this.correctAnswerNum = correctAnswerNum;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getAnswers() {
        return answers;
    }

    public int getCorrectAnswerNum() {
        return correctAnswerNum;
    }
}