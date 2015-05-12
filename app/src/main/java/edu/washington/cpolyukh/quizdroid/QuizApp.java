package edu.washington.cpolyukh.quizdroid;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class QuizApp extends android.app.Application {
    private static QuizApp instance;
    public static final String TAG = "QuizApp";
    private TopicRepository topicsRepository;
    private static final boolean readFromJSON = true;

    public static QuizApp getInstance() {
        if (instance == null) {
            instance = new QuizApp();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;

        super.onCreate();
        Log.i(TAG, TAG + " loading correctly");

        topicsRepository = HardCodedRepository.getInstance();

        // Fetch data.json in assets/ folder
        try {
            List<Topic> topicsToAdd;
            if (readFromJSON) {
                topicsToAdd = createJSONList();
            } else {
                topicsToAdd = QuizConstants.getTopicList();
            }

            topicsRepository.addListOfTopics(topicsToAdd);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<Topic> createJSONList() throws JSONException, IOException{

        String json = null;
        this.topicsRepository = HardCodedRepository.getInstance();
        InputStream inputStream = getAssets().open("questions.json");
        json = readJSONFile(inputStream);

        JSONArray jsonArray = new JSONArray(json);

        List<Topic> newTopicList = new ArrayList<Topic>();

        for (int i = 0; i < jsonArray.length(); i++)  {

            Log.i("QuizApp", "1");

            JSONObject currentJSONObject = (JSONObject) jsonArray.get(i);

            Log.i("QuizApp", "2");

            String title = currentJSONObject.getString("title");
            Log.i("QuizApp", "3");

            String desc = currentJSONObject.getString("desc");

            Log.i("QuizApp", "4");
            JSONArray questionArray = currentJSONObject.getJSONArray("questions");

            Log.i("QuizApp", "5");

            List<Question> currentQuestions = new ArrayList<Question>();

            Log.i("QuizApp", "6");

            for (int j = 0; j < questionArray.length(); j++) {
                JSONObject currentQuestionJSONObject = (JSONObject) questionArray.get(j);

                String text = currentQuestionJSONObject.getString("text");
                int answer = currentQuestionJSONObject.getInt("answer");
                JSONArray answerJSONArray = currentQuestionJSONObject.getJSONArray("answers");

                String[] answerArray = new String[4];

                for (int k = 0; k < answerJSONArray.length(); k++) {
                    answerArray[k] = answerJSONArray.getString(k);
                }

                Question currentQuestionObject = new Question(text, answerArray, answer);

                currentQuestions.add(currentQuestionObject);
            }

            Topic currentTopicObject = new Topic(title, desc, currentQuestions);

            newTopicList.add(currentTopicObject);
        }

        return newTopicList;
    }


    public QuizApp() {
        if (instance == null) {
            instance = this;
        } else {
            Log.e("MyApp", "There is an error. You tried to create more than 1 QuizApp");
        }
    }

    // reads InputStream of JSON file and returns the file in JSON String format
    public String readJSONFile(InputStream inputStream) throws IOException {

        int size = inputStream.available();
        byte[] buffer = new byte[size];
        inputStream.read(buffer);
        inputStream.close();

        return new String(buffer, "UTF-8");
    }

    public TopicRepository getRepository() throws IOException, JSONException {
        if (topicsRepository == null) {
            topicsRepository = HardCodedRepository.getInstance();
        }
        return topicsRepository;
    }
}