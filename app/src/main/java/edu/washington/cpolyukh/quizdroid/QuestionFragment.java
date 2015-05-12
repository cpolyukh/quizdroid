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

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;


public class QuestionFragment extends Fragment {

    public static int questionsAnswered = 0;
    public static int correctAnswers = 0;
    private String quizTopic;
    private Activity hostActivity;

    public QuestionFragment() {
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
        // Inflate the layout for this fragment
        final View fragmentView = inflater.inflate(R.layout.activity_question_fragment, container, false);
        container.removeAllViews();

        QuizApp quizApp = QuizApp.getInstance();

        try {
            TopicRepository repository = quizApp.getRepository();
            List<Topic> topicList = repository.getAllTopics();
            Topic currentTopic = repository.getTopicByName(quizTopic);
            List<Question> questionList = currentTopic.getQuestions();
            final int count = currentTopic.getQuestionCount();

            quizTopic = getArguments().getString("topic");
            Log.i("QuestionFragment", quizTopic);

            if (questionsAnswered >= count) {
                questionsAnswered = count - 1;
            }

            Question currentQuestion = questionList.get(questionsAnswered);
            String currentQuestionString = currentQuestion.getQuestion();
            final String[] currentAnswers = currentQuestion.getAnswers();
            final int answerIndex = currentQuestion.getCorrectAnswerNum() - 1;

            setValues(currentQuestionString, currentAnswers, fragmentView);

            Button submit = (Button) fragmentView.findViewById(R.id.btnSubmit);

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hostActivity instanceof QuizActivity) {
                        Log.i("QuestionFragment", "Button clicked!");
                        ((QuizActivity) hostActivity).loadAnswerFrag();

                        RadioGroup quizChoices = (RadioGroup) fragmentView.findViewById(R.id.quizChoices);
                        int selectedAnswerId = quizChoices.getCheckedRadioButtonId();
                        if (selectedAnswerId != -1) {
                            questionsAnswered++;
                            Log.i("QuestionFragment", "" + questionsAnswered);
                            RadioButton selectedAnswer = (RadioButton) fragmentView.findViewById(selectedAnswerId);

                            String answerText = (String) selectedAnswer.getText();
                            String correctAnswer = currentAnswers[answerIndex];

                            if (answerText.equals(correctAnswer)) {
                                correctAnswers++;
                            }

                            FragmentManager fm = getFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();

                            Bundle topicBundle = new Bundle();
                            topicBundle.putString("topic", quizTopic);
                            topicBundle.putInt("correct answers", correctAnswers);
                            topicBundle.putInt("questions answered", questionsAnswered);
                            topicBundle.putString("your answer", answerText);
                            topicBundle.putString("correct answer", correctAnswer);
                            topicBundle.putInt("total questions", count);

                            AnswerFragment answerFragment = new AnswerFragment();
                            answerFragment.setArguments(topicBundle);

                            ft.add(R.id.questionContainer, answerFragment);
                            ft.commit();
                        }
                    }
                }
            });
        } catch (IOException i) {

        } catch (JSONException je) {

        }


        return fragmentView;
    }

    public void setValues(String currentQuestionString, String[] currentAnswers, View v) {
        RadioButton rbAns1 = (RadioButton)v.findViewById(R.id.rbAns1);
        RadioButton rbAns2 = (RadioButton)v.findViewById(R.id.rbAns2);
        RadioButton rbAns3 = (RadioButton)v.findViewById(R.id.rbAns3);
        RadioButton rbAns4 = (RadioButton)v.findViewById(R.id.rbAns4);
        TextView txtQuestion = (TextView)v.findViewById(R.id.txtQuestion);

        txtQuestion.setText(currentQuestionString);

        int i = 0;
        for (String currentAnswer : currentAnswers) {

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
