package edu.washington.cpolyukh.quizdroid;

import android.app.DownloadManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class QuizApp extends android.app.Application {
    private static QuizApp instance;
    public static final String TAG = "QuizApp";
    private TopicRepository topicsRepository;
    private static final boolean readFromJSON = true;
    private DownloadManager dm;
    private long enqueue;
    public static String URL = "http://tednewardsandbox.site44.com/questions.json";
    public static int minutes = 5;

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

        DownloadService.startOrStopAlarm(this, true);
    }

    public List<Topic> createJSONList() throws JSONException, IOException {
        DownloadService.startOrStopAlarm(this, true);

        /*String FILENAME = "hello_file";
        String string = "hello world!";

        FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
        fos.write(string.getBytes());
        fos.close();

        Call openFileInput() and pass it the name of the file to read. This returns a FileInputStream.
        Read bytes from the file with read().
                Then close the stream with close().*/


        String FILE_NAME = "questions.json";
        //FileInputStream inputStream = openFileInput(FILE_NAME);

        File myFile = new File(getFilesDir().getAbsolutePath(), "/" + FILE_NAME);

        String json = null;
        this.topicsRepository = HardCodedRepository.getInstance();
        //Log.i("QuizApp", "file path: " + getFilesDir().getAbsolutePath());
        //File file = new File(getFilesDir().getAbsolutePath() + "/questions.json");
        //InputStream inputStream = new FileInputStream(file);
        //InputStream inputStream2 = getAssets().open(getFilesDir().getAbsolutePath() + "questions.json");
        //json = readJSONFile(inputStream);

        if (myFile.exists()) {
            Log.i("MyApp", "data.json DOES exist");

            try {
                FileInputStream fis = openFileInput("questions.json");      // sweet we found it. openFileInput() takes a string path from your data directory. no need to put 'data/' in your path parameter
                json = readJSONFile(fis);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {
            // Can't find data.json file in files directory. Fetch data.json in assets
            Log.i("MyApp", "data.json DOESN'T exist. Fetch from assets");

            try {
                InputStream inputStream = getAssets().open("questions.json");
                json = readJSONFile(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        JSONArray jsonArray = new JSONArray(json);

        List<Topic> newTopicList = new ArrayList<Topic>();

        for (int i = 0; i < jsonArray.length(); i++)  {
            JSONObject currentJSONObject = (JSONObject) jsonArray.get(i);

            String title = currentJSONObject.getString("title");
            String desc = currentJSONObject.getString("desc");
            JSONArray questionArray = currentJSONObject.getJSONArray("questions");
            List<Question> currentQuestions = new ArrayList<Question>();

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

    public void updateRepository() throws JSONException, IOException {
        createJSONList();
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