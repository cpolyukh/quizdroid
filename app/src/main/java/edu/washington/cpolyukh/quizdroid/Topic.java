package edu.washington.cpolyukh.quizdroid;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by christina3135 on 5/6/2015.
 */
class Topic {
    private String topicName;
    private String shortDescription;
    private String longDescription;
    private List<Question> questions;

    public String getTopicName() {
        return topicName;
    }

    public Topic(String topicName) {
        this(topicName, null, null, new ArrayList<Question>());
    }

    public Topic(String topicName, String shortDescription) {
        this(topicName, shortDescription, null, new ArrayList<Question>());
    }

    public Topic(String topicName, String shortDescription, String longDescription) {
        this(topicName, shortDescription, longDescription, new ArrayList<Question>());
    }

    public Topic(String topicName, String longDescription, List<Question> questions) {
        this(topicName, null, longDescription, questions);
    }

    public Topic(String topicName, String shortDescription, String longDescription, List<Question> questions) {
        this.topicName = topicName;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.questions = questions;
    }

    public void addQuestion(Question newQuestion) {
        questions.add(newQuestion);
    }

    public int getQuestionCount() {
        return questions.size();
    }


    public String getLongDescription() {
        return longDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public List<Question> getQuestions() {
        return questions;
    }
}
