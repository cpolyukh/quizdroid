package edu.washington.cpolyukh.quizdroid;

import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by christina3135 on 4/23/2015.
 */
public class QuizConstants {
    public static final HashMap<String, String> topicsToDescriptions = new HashMap<String, String>() {{
        put("Math", "Test your numerical knowledge!");
        put("Physics", "I don't know much about physics besides that" +
                " gravity is a thing but let's see what you know");
        put("Marvel Superheroes", "Superheroes! How much of your" +
                " childhood was spent with comic books?");
    }};

    public static final HashMap<String, TreeMap<String, HashMap<String, Boolean>>> topicsToQuestions
            = new HashMap<String, TreeMap<String, HashMap<String, Boolean>>>() {{
        put("Math", new TreeMap<String, HashMap<String, Boolean>>() {{
            put("10^3 = ?", new HashMap<String, Boolean>() {{
                put("yes", false);
                put("10", false);
                put("1000", true);
                put("-10", false);
            }});
            put("0 * 16 = ?", new HashMap<String, Boolean>() {{
                put("0", true);
                put("1", false);
                put("16", false);
                put("160", false);
            }});
            put("Which of the following is a form of math?", new HashMap<String, Boolean>() {{
                put("cats", false);
                put("algebra", true);
                put("La-Z-boy", false);
                put("Harry Potter", false);
            }});
            put("Which of the following is a trigonometic function?", new HashMap<String, Boolean>() {{
                put("sin", false);
                put("cos", false);
                put("tan", false);
                put("all of the above", true);
            }});
            put("2 + 2 = ?", new HashMap<String, Boolean>() {{
                put("4", true);
                put("yellow", false);
                put("green", false);
                put("blue", false);
            }});
        }});
        put("Physics", new TreeMap<String, HashMap<String, Boolean>>() {{
            put("What goes up must come ____", new HashMap<String, Boolean>() {{
                put("further up", false);
                put("a little to the left", false);
                put("to my birthday party", false);
                put("down", true);
            }});
            put("Which of these men is a known physicist?", new HashMap<String, Boolean>() {{
                put("Barney the purple dinosaur", false);
                put("Stephen Hawking", true);
                put("Ted Neward", false);
                put("Barack Obama", false);
            }});
            put("Which theory is Albert Einstein responsible for?", new HashMap<String, Boolean>() {{
                put("The theory of relativity", true);
                put("The theory of why college students are so broke", false);
                put("The theory of evolution", false);
                put("All theories", false);
            }});
            put("Which of the following relates most closely to physics?", new HashMap<String, Boolean>() {{
                put("math", true);
                put("ballet", false);
                put("gardening", false);
                put("psychology", false);
            }});
        }});
        put("Marvel Superheroes", new TreeMap<String, HashMap<String, Boolean>>() {{
            put("Which of the following is a Marvel Superhero?", new HashMap<String, Boolean>() {{
                put("Nicki Minaj", false);
                put("Blue from Blue's Clues", false);
                put("George Harrison", false);
                put("Iron Man", true);
            }});
            put("Which of the following is not one of the Avengers?", new HashMap<String, Boolean>() {{
                put("Captain America", false);
                put("Black Widow", false);
                put("Batman", true);
                put("Thor", false);
            }});
            put("When was the first Marvel comic book published?", new HashMap<String, Boolean>() {{
                put("1906", false);
                put("1964", false);
                put("1939", true);
                put("2004", false);
            }});
            put("Spiderman is a Marvel Superhero", new HashMap<String, Boolean>() {{
                put("yes", true);
                put("only on leap years", false);
                put("it depends", false);
                put("no", false);
            }});
        }});
    }};
}