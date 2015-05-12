package edu.washington.cpolyukh.quizdroid;

import android.provider.MediaStore;
import android.util.Log;

import junit.framework.Test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/*import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;*/

/**
 * Created by christina3135 on 5/7/2015.
 */
public class HardCodedRepository implements TopicRepository {
    private List<Topic> topics;
    private static final boolean readFromJSON = true;
    private static HardCodedRepository instance;

    public static HardCodedRepository getInstance() {
        if (instance == null) {
            instance = new HardCodedRepository();
        }
        return instance;
    }

    private HardCodedRepository() {
        topics = new ArrayList<Topic>();
    }

    public void addTopic(Topic newTopic) {
        topics.add(newTopic);
    }

    public void addListOfTopics(List<Topic> topics) {
        this.topics.addAll(topics);
    }

    public List<Topic> getAllTopics() throws IOException, JSONException {
        if (topics == null) {
            instance = new HardCodedRepository();
        }
        return topics;
    }

    public List<String> getTopicNames() {
        List<String> topicNames = new ArrayList<String>();

        for (Topic currentTopic : topics) {
            topicNames.add(currentTopic.getTopicName());
        }

        return topicNames;
    }

    public Topic getTopicByName(String name) {
        for (Topic currentTopic : topics) {
            if (currentTopic.getTopicName().equals(name)) {
                return currentTopic;
            }
        }

        return null;
    }

    public int getTopicCount() {
        return topics.size();
    }
}
