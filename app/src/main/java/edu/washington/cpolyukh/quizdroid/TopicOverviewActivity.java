package edu.washington.cpolyukh.quizdroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;


public class TopicOverviewActivity extends ActionBarActivity {

    //HashMap<String, String> topicMap = QuizConstants.topicsToDescriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_overview);

        Intent quizIntent = getIntent();
        final String quizTopic = quizIntent.getStringExtra("topic");

        this.getIntent();

        String description = QuizConstants.topicsToDescriptions.get(quizTopic);
        int count = QuizConstants.topicsToQuestions.get(quizTopic).size();

        TextView txtTopic = (TextView)findViewById(R.id.txtTopic);
        TextView txtDesc = (TextView)findViewById(R.id.txtDescr);
        TextView txtNumQues = (TextView)findViewById(R.id.txtNumQues);

        txtTopic.setText(quizTopic);
        txtDesc.setText(description);
        txtNumQues.setText("Number of questions in this quiz: " + count);

        Button beginQuiz = (Button)findViewById(R.id.btnStart);
        beginQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takeQuiz = new Intent(TopicOverviewActivity.this, TakeQuizActivity.class);
                takeQuiz.putExtra("topic", quizTopic);

                startActivity(takeQuiz);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_topic_overview, menu);
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
