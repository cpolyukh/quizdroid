package edu.washington.cpolyukh.quizdroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class AnswerActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        Intent quizIntent = getIntent();
        final String quizTopic = quizIntent.getStringExtra("topic");
        final int correctAnswers = quizIntent.getIntExtra("correct answers", 0);
        final int questionsAnswered = quizIntent.getIntExtra("questions answered", 1);
        final String userAnswer = quizIntent.getStringExtra("your answer");
        final String correctAnswer = quizIntent.getStringExtra("correct answer");
        final int totalQuestions = quizIntent.getIntExtra("total questions", 1);

        this.getIntent();

        TextView txtUserAnswer = (TextView)findViewById(R.id.txtUserAnswer);
        TextView txtCorrectAnswer = (TextView)findViewById(R.id.txtCorrectAnswer);
        TextView txtNumCorrect = (TextView)findViewById(R.id.txtNumCorrect);

        txtUserAnswer.setText("Your answer: " + userAnswer);
        txtCorrectAnswer.setText("Correct answer: " + correctAnswer);
        txtNumCorrect.setText("You have answered " + correctAnswers + " out of " +
                questionsAnswered + " questions correctly so far");

        String buttonText = "Next";
        if (totalQuestions == questionsAnswered) {
            buttonText = "Finish";
        }

        Button btnNext = (Button)findViewById(R.id.btnNext);
        btnNext.setText(buttonText);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalQuestions > questionsAnswered) {
                    Intent takeQuiz = new Intent(AnswerActivity.this, TakeQuizActivity.class);
                    takeQuiz.putExtra("topic", quizTopic);

                    startActivity(takeQuiz);
                } else {
                    Intent takeQuiz = new Intent(AnswerActivity.this, MainActivity.class);

                    startActivity(takeQuiz);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_answer, menu);
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
