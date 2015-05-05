package edu.washington.cpolyukh.quizdroid;

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

import java.util.HashMap;


public class TopicActivity extends FragmentActivity {

    private HashMap<String, String> topicMap = QuizConstants.topicsToDescriptions;
    private String[] topics = topicMap.keySet().toArray(new String[topicMap.size()]);
    private ListView topicList;
    public static final String MAIN_ACTIVITY = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        topicList = (ListView) findViewById(R.id.lstTopics);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_list_item, topics);
        topicList.setAdapter(adapter);

        topicList.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currentTopic = adapter.getItem(position);

                Intent overview = new Intent(TopicActivity.this, QuizActivity.class);
                overview.putExtra("topic", currentTopic);

                startActivity(overview);

                finish();
            }
        });
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
