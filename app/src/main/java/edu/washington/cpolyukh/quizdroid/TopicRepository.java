package edu.washington.cpolyukh.quizdroid;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;

/**
 * Created by christina3135 on 5/6/2015.
 */
public interface TopicRepository {
    public void addTopic(Topic newTopic);

    public void addListOfTopics(List<Topic> topics);

    public Topic getTopicByName(String name);

    public List<String> getTopicNames();

    public int getTopicCount();

    public List<Topic> getAllTopics() throws IOException, JSONException;
}

