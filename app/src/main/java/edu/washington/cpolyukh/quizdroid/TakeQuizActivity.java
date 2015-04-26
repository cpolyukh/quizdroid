package edu.washington.cpolyukh.quizdroid;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;


public class TakeQuizActivity extends ActionBarActivity {

    private static int questionsAnswered = 0;
    private static int correctAnswers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_quiz);

        Intent quizIntent = getIntent();
        final String quizTopic = quizIntent.getStringExtra("topic");

        this.getIntent();

        final TreeMap<String, HashMap<String, Boolean>> questionsAndAnswers = QuizConstants.topicsToQuestions.get(quizTopic);

        String[] questions = questionsAndAnswers.keySet().toArray(new String[questionsAndAnswers.size()]);

        String currentQuestion = questions[questionsAnswered];
        final HashMap<String, Boolean> currentAnswers = questionsAndAnswers.get(currentQuestion);

        setValues(currentQuestion, currentAnswers);

        Button submit = (Button)findViewById(R.id.btnSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup quizChoices = (RadioGroup) findViewById(R.id.quizChoices);
                int selectedAnswerId = quizChoices.getCheckedRadioButtonId();
                if (selectedAnswerId != -1) {
                    questionsAnswered++;
                    RadioButton selectedAnswer = (RadioButton)findViewById(selectedAnswerId);

                    String answerText = (String)selectedAnswer.getText();
                    String correctAnswer = findCorrectAnswer(currentAnswers);

                    if (answerText.equals(correctAnswer)) {
                        correctAnswers++;
                    }

                    Intent seeAnswers = new Intent(TakeQuizActivity.this, AnswerActivity.class);
                    seeAnswers.putExtra("topic", quizTopic);
                    seeAnswers.putExtra("correct answers", correctAnswers);
                    seeAnswers.putExtra("questions answered", questionsAnswered);
                    seeAnswers.putExtra("your answer", answerText);
                    seeAnswers.putExtra("correct answer", correctAnswer);
                    seeAnswers.putExtra("total questions", questionsAndAnswers.size());

                    startActivity(seeAnswers);
                }
            }
        });
    }


    public String findCorrectAnswer(HashMap<String, Boolean> currentAnswers) {
        String correctAnswer = null;

        for (String currentAnswer : currentAnswers.keySet()) {
            if (currentAnswers.get(currentAnswer)) {
                correctAnswer = currentAnswer;
            }
        }

        return correctAnswer;
    }

    public void setValues(String currentQuestion, HashMap<String, Boolean> currentAnswers) {
        RadioButton rbAns1 = (RadioButton)findViewById(R.id.rbAns1);
        RadioButton rbAns2 = (RadioButton)findViewById(R.id.rbAns2);
        RadioButton rbAns3 = (RadioButton)findViewById(R.id.rbAns3);
        RadioButton rbAns4 = (RadioButton)findViewById(R.id.rbAns4);
        TextView txtQuestion = (TextView)findViewById(R.id.txtQuestion);

        txtQuestion.setText(currentQuestion);

        int i = 0;
        for (String currentAnswer : currentAnswers.keySet()) {

            if (i == 0) {
                rbAns1.setText(currentAnswer);
            } else if (i == 1) {
                rbAns2.setText(currentAnswer);
            } else if (i == 2) {
                rbAns3.setText(currentAnswer);
            } else {
                rbAns4.setText(currentAnswer);
            }

            i++;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_take_quiz, menu);
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
