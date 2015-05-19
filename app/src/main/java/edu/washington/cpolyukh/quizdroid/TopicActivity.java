package edu.washington.cpolyukh.quizdroid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;


public class TopicActivity extends Activity {

    private List<Topic> topicList;
    private ListView topicListView;
    public static final String MAIN_ACTIVITY = "MainActivity";
    private static final int SETTINGS_RESULT = 1;
    private String URL = "http://tednewardsandbox.site44.com/questions.json";
    private int minutes = 5;
    private QuizApp quizApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        quizApp = QuizApp.getInstance();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        URL = sharedPrefs.getString("prefURL", "http://tednewardsandbox.site44.com/questions.json");
        minutes = Integer.parseInt(sharedPrefs.getString("prefMinutes", "5"));

        QuizApp.getInstance().URL = URL;
        QuizApp.getInstance().minutes = minutes;

        AlarmReceiver.URL = URL;

        try {
            TopicRepository repository = quizApp.getRepository();
            topicList = repository.getAllTopics();
            List<String> topicNames = repository.getTopicNames();

            Button btnSettings = (Button) findViewById(R.id.btnSettings);
            btnSettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), UserSettingActivity.class);
                    startActivityForResult(i, SETTINGS_RESULT);
                }

            });

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        boolean currentlyDownloading = false; //to be updated when we actually include download

        if(requestCode == SETTINGS_RESULT && !currentlyDownloading) {
            displayUserSettings();
        }
    }

    private void displayUserSettings() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        URL = sharedPrefs.getString("prefURL", "http://tednewardsandbox.site44.com/questions.json");
        minutes = Integer.parseInt(sharedPrefs.getString("prefMinutes", "5"));

        QuizApp.getInstance().URL = URL;
        QuizApp.getInstance().minutes = minutes;

        AlarmReceiver.URL = URL;
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
