package edu.washington.cpolyukh.quizdroid;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;


public class TopicActivity extends Activity {

    private List<Topic> topicList;
    private ListView topicListView;
    public static final String MAIN_ACTIVITY = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        QuizApp quizApp = QuizApp.getInstance();

        try {
            TopicRepository repository = quizApp.getRepository();
            topicList = repository.getAllTopics();
            List<String> topicNames = repository.getTopicNames();


            topicListView = (ListView) findViewById(R.id.lstTopics);

            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_list_item, topicNames);
            topicListView.setAdapter(adapter);

            topicListView.setOnItemClickListener(new ListView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String currentTopic = adapter.getItem(position);

                    Intent overview = new Intent(TopicActivity.this, QuizActivity.class);
                    overview.putExtra("topic", currentTopic);

                    startActivity(overview);

                    finish();
                }
            });
        } catch (IOException i) {

        } catch (JSONException je) {

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_topic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
