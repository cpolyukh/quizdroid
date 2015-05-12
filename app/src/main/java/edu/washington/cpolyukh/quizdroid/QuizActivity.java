package edu.washington.cpolyukh.quizdroid;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;


public class QuizActivity extends Activity {
    private String quizTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Intent whoCalledMe = getIntent();

        if (whoCalledMe != null) {
            quizTopic = whoCalledMe.getStringExtra("topic");

            QuizApp quizApp = QuizApp.getInstance();
            try {
                TopicRepository repository = quizApp.getRepository();
                List<Topic> topicList = repository.getAllTopics();
                Topic currentTopic = repository.getTopicByName(quizTopic);
                List<Question> questionList = currentTopic.getQuestions();
                String description = currentTopic.getLongDescription();
                int count = currentTopic.getQuestionCount();

                TextView txtTopic = (TextView) findViewById(R.id.txtTopic);
                TextView txtDesc = (TextView) findViewById(R.id.txtDescr);
                TextView txtNumQues = (TextView) findViewById(R.id.txtNumQues);

                txtTopic.setText(quizTopic);
                txtDesc.setText(description);
                txtNumQues.setText("Number of questions in this quiz: " + count);

                Button beginQuiz = (Button) findViewById(R.id.btnStart);
                beginQuiz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Loads QuestionFragment
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();

                        Bundle topicBundle = new Bundle();
                        topicBundle.putString("topic", quizTopic);

                        QuestionFragment questionFragment = new QuestionFragment();
                        questionFragment.setArguments(topicBundle);

                        ft.add(R.id.container, questionFragment);
                        ft.commit();
                    }
                });
            } catch (IOException i) {

            } catch (JSONException je) {

            }
        }
    }

    public void loadAnswerFrag() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Bundle topicBundle = new Bundle();
        topicBundle.putString("topic", quizTopic);

        QuestionFragment questionFragment = new QuestionFragment();
        questionFragment.setArguments(topicBundle);

        ft.add(R.id.container, questionFragment);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
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
