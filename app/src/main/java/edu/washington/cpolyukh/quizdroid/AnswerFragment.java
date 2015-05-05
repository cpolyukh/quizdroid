package edu.washington.cpolyukh.quizdroid;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.TreeMap;


public class AnswerFragment extends Fragment {

    private String quizTopic;
    private Activity hostActivity;

    public AnswerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            quizTopic = getArguments().getString("topic");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("AnswerFragment", "answer fragment reached");
        // Inflate the layout for this fragment
        final View fragmentView = inflater.inflate(R.layout.activity_answer_fragment, container, false);
        container.removeAllViews();

        quizTopic = getArguments().getString("topic");
        final int correctAnswers = getArguments().getInt("correct answers", 0);
        final int questionsAnswered = getArguments().getInt("questions answered", 1);
        final String userAnswer = getArguments().getString("your answer");
        final String correctAnswer = getArguments().getString("correct answer");
        final int totalQuestions = getArguments().getInt("total questions", 1);

        TextView txtUserAnswer = (TextView)fragmentView.findViewById(R.id.txtUserAnswer);
        TextView txtCorrectAnswer = (TextView)fragmentView.findViewById(R.id.txtCorrectAnswer);
        TextView txtNumCorrect = (TextView)fragmentView.findViewById(R.id.txtNumCorrect);

        txtUserAnswer.setText("Your answer: " + userAnswer);
        txtCorrectAnswer.setText("Correct answer: " + correctAnswer);
        txtNumCorrect.setText("You have answered " + correctAnswers + " out of " +
                questionsAnswered + " questions correctly so far");

        String buttonText = "Next";
        if (totalQuestions == questionsAnswered) {
            buttonText = "Finish";
        }

        Button btnNext = (Button)fragmentView.findViewById(R.id.btnNext);
        btnNext.setText(buttonText);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalQuestions > questionsAnswered) {
                    Log.i("AnswerFragment", "first branch entered");
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();

                    Bundle topicBundle = new Bundle();
                    topicBundle.putString("topic", quizTopic);

                    QuestionFragment questionFragment = new QuestionFragment();
                    questionFragment.setArguments(topicBundle);

                    ft.add(R.id.answerContainer, questionFragment);
                    ft.commit();
                } else {
                    Log.i("AnswerFragment", "else branch entered");
                    QuestionFragment.questionsAnswered = 0;
                    QuestionFragment.correctAnswers = 0;
                    Intent newQuiz = new Intent(getActivity(), TopicActivity.class);
                    getActivity().startActivity(newQuiz);
                }
            }
        });

        return fragmentView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.hostActivity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
