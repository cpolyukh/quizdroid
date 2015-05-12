package edu.washington.cpolyukh.quizdroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by christina3135 on 4/23/2015.
 */
public class QuizConstants {
    private static List<Topic> topicList;

    public static final HashMap<String, String[]> topicsToDescriptions = new HashMap<String, String[]>() {{
        put("Math", new String[]{"Numbers and stuff", "Test your numerical knowledge!"});
        put("Physics", new String[]{"Physical things", "I don't know much about physics besides that" +
                " gravity is a thing but let's see what you know"});
        put("Marvel Superheroes",  new String[]{"Superhero quiz", "Superheroes! How much of your" +
                " childhood was spent with comic books?"});
    }};

    public static final HashMap<String, TreeMap<String, TreeMap<String, Boolean>>> topicsToQuestions
            = new HashMap<String, TreeMap<String, TreeMap<String, Boolean>>>() {{
        put("Math", new TreeMap<String, TreeMap<String, Boolean>>() {{
            put("10^3 = ?", new TreeMap<String, Boolean>() {{
                put("yes", false);
                put("10", false);
                put("1000", true);
                put("-10", false);
            }});
            put("0 * 16 = ?", new TreeMap<String, Boolean>() {{
                put("0", true);
                put("1", false);
                put("16", false);
                put("160", false);
            }});
            put("Which of the following is a form of math?", new TreeMap<String, Boolean>() {{
                put("cats", false);
                put("algebra", true);
                put("La-Z-boy", false);
                put("Harry Potter", false);
            }});
            put("Which of the following is a trigonometic function?", new TreeMap<String, Boolean>() {{
                put("sin", false);
                put("cos", false);
                put("tan", false);
                put("all of the above", true);
            }});
            put("2 + 2 = ?", new TreeMap<String, Boolean>() {{
                put("4", true);
                put("yellow", false);
                put("green", false);
                put("blue", false);
            }});
        }});
        put("Physics", new TreeMap<String, TreeMap<String, Boolean>>() {{
            put("What goes up must come ____", new TreeMap<String, Boolean>() {{
                put("further up", false);
                put("a little to the left", false);
                put("to my birthday party", false);
                put("down", true);
            }});
            put("Which of these men is a known physicist?", new TreeMap<String, Boolean>() {{
                put("Barney the purple dinosaur", false);
                put("Stephen Hawking", true);
                put("Ted Neward", false);
                put("Barack Obama", false);
            }});
            put("Which theory is Albert Einstein responsible for?", new TreeMap<String, Boolean>() {{
                put("The theory of relativity", true);
                put("The theory of why college students are so broke", false);
                put("The theory of evolution", false);
                put("All theories", false);
            }});
            put("Which of the following relates most closely to physics?", new TreeMap<String, Boolean>() {{
                put("math", true);
                put("ballet", false);
                put("gardening", false);
                put("psychology", false);
            }});
        }});
        put("Marvel Superheroes", new TreeMap<String, TreeMap<String, Boolean>>() {{
            put("Which of the following is a Marvel Superhero?", new TreeMap<String, Boolean>() {{
                put("Nicki Minaj", false);
                put("Blue from Blue's Clues", false);
                put("George Harrison", false);
                put("Iron Man", true);
            }});
            put("Which of the following is not one of the Avengers?", new TreeMap<String, Boolean>() {{
                put("Captain America", false);
                put("Black Widow", false);
                put("Batman", true);
                put("Thor", false);
            }});
            put("When was the first Marvel comic book published?", new TreeMap<String, Boolean>() {{
                put("1906", false);
                put("1964", false);
                put("1939", true);
                put("2004", false);
            }});
            put("Spiderman is a Marvel Superhero", new TreeMap<String, Boolean>() {{
                put("yes", true);
                put("only on leap years", false);
                put("it depends", false);
                put("no", false);
            }});
        }});
    }};

    public static List<Topic> getTopicList() {
        if (topicList == null) {
            topicList = createTopicList();
        }
        return topicList;
    }

    private static List<Topic> createTopicList() {
        List<Topic> newTopicList = new ArrayList<Topic>();

        for (String currentTopic : topicsToDescriptions.keySet()) {
            String[] descriptions = topicsToDescriptions.get(currentTopic);
            String shortDesc = descriptions[0];
            String longDesc = descriptions[1];
            List<Question> newQuestionList = new ArrayList<Question>();

            TreeMap<String, TreeMap<String, Boolean>> questionMap = topicsToQuestions.get(currentTopic);
            for (String currentQuestion : questionMap.keySet()) {
                TreeMap<String, Boolean> answerMap = questionMap.get(currentQuestion);
                int index = 0;
                int correctAnswerNum = 1;
                String[] answers = new String[4];

                for (String currentAnswer : answerMap.keySet()) {
                    answers[index] = currentAnswer;

                    if (answerMap.get(currentAnswer)) {
                        correctAnswerNum = index + 1;
                    }

                    index++;
                }

                Question newQuestion = new Question(currentQuestion, answers, correctAnswerNum);
                newQuestionList.add(newQuestion);
            }

            Topic newTopic = new Topic(currentTopic, shortDesc, longDesc, newQuestionList);
            newTopicList.add(newTopic);
        }

        return newTopicList;
    }
}