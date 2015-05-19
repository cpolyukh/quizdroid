package edu.washington.cpolyukh.quizdroid;

import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class UserSettingActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // add the xml resource
        addPreferencesFromResource(R.xml.user_settings);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String URL = sharedPrefs.getString("prefURL", "http://tednewardsandbox.site44.com/questions.json");
        int minutes = Integer.parseInt(sharedPrefs.getString("prefMinutes", "5"));

        Log.i("UserSettingActivity", URL);
        Log.i("UserSettingActivity", "" + minutes);

        QuizApp.getInstance().URL = URL;
        QuizApp.getInstance().minutes = minutes;

        AlarmReceiver.URL = URL;

        Log.i("UserSettingActivity", "QuizApp URL: " + QuizApp.getInstance().URL);
        Log.i("UserSettingActivity", "QuizApp minutes: " + QuizApp.getInstance().minutes);

    }

}