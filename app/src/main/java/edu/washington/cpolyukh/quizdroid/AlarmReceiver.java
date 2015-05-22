package edu.washington.cpolyukh.quizdroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver{
    public static String URL = QuizApp.getInstance().URL;

    public AlarmReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("AlarmReceiver", "entered onReceive() from AlarmReceiver");

        // This is where we start our DownloadService class! aka tell our IntentService to start the download!
        Intent downloadServiceIntent = new Intent(context, DownloadService.class);
        context.startService(downloadServiceIntent);

        //Toast.makeText(context, URL, Toast.LENGTH_SHORT).show();
    }
}