package edu.washington.cpolyukh.quizdroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.provider.Settings;
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
import android.widget.Toast;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    private DownloadManager dm;
    private long enqueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE); // Add more filters here that you want the receiver to listen to
        registerReceiver(receiver, filter);

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

    // This is your receiver that you registered in the onCreate that will receive any messages that match a download-complete like broadcast
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final Context innerContext = context;
            final Intent innerIntent = intent;

            boolean connected = false;
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                //we are connected to a network
                connected = true;
            }

            if (!connected) {
                Log.i("TopicActivity", "Error: No internet connection");
                if (Settings.System.getInt(context.getContentResolver(),
                        Settings.System.AIRPLANE_MODE_ON, 0) != 0) {
                    //not connected to internet because in airplane mode

                    //direct user to settings
                    Log.i("TopicActivity", "Failed: Airplane mode is on");

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder
                            .setTitle("Go to settings to turn off airplane mode")
                            .setMessage("Are you sure?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Yes button clicked, do something
                                    Intent settings = new Intent(TopicActivity.this, Settings.class);

                                    startActivity(settings);

                                    finish();

                                    //Toast.makeText(TopicActivity.this, "Yes button pressed",
                                    //        Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("No", null)						//Do nothing on no
                            .show();
                } else {
                    //not connected to internet for other reasons
                    Log.i("TopicActivity", "Failed: no internet connection");
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder
                            .setTitle("No internet connection found")				//Do nothing on no
                            .show();
                }
            } else {
                String action = intent.getAction();

                dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

                Log.i("MyApp BroadcastReceiver", "onReceive of registered download reciever");

                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    Log.i("MyApp BroadcastReceiver", "download complete!");
                    long downloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);

                    // if the downloadID exists
                    if (downloadID != 0) {

                        // Check status
                        DownloadManager.Query query = new DownloadManager.Query();
                        query.setFilterById(downloadID);
                        Cursor c = dm.query(query);
                        if (c.moveToFirst()) {
                            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                            Log.d("DM Sample", "Status Check: " + status);
                            switch (status) {
                                case DownloadManager.STATUS_PAUSED:
                                case DownloadManager.STATUS_PENDING:
                                case DownloadManager.STATUS_RUNNING:
                                    break;
                                case DownloadManager.STATUS_SUCCESSFUL:
                                    // The download-complete message said the download was "successfu" then run this code
                                    ParcelFileDescriptor file;
                                    StringBuffer strContent = new StringBuffer("");

                                    try {
                                        // Get file from Download Manager (which is a system service as explained in the onCreate)
                                        file = dm.openDownloadedFile(downloadID);
                                        FileInputStream fis = new FileInputStream(file.getFileDescriptor());

                                        StringBuilder builder = new StringBuilder();
                                        int ch;
                                        while ((ch = fis.read()) != -1) {
                                            builder.append((char) ch);
                                        }

                                        String fileText = builder.toString();

                                        // YOUR CODE HERE [write string to data/data.json]
                                        //      [hint, i wrote a writeFile method in MyApp... figure out how to call that from inside this Activity]

                                        // convert your json to a string and echo it out here to show that you did download it

                                        writeToFile(fileText);

                                    /*
                                    String jsonString = ....myjson...to string().... chipotle burritos.... blah
                                    Log.i("MyApp - Here is the json we download:", jsonString);
                                    */

                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case DownloadManager.STATUS_FAILED:
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder
                                            .setTitle("Download failed; try again?")
                                            .setMessage("Are you sure?")
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //Yes button clicked, do something
                                                    onReceive(innerContext, innerIntent);
                                                }
                                            })
                                            .setNegativeButton("No", null)						//Do nothing on no
                                            .show();
                                    break;
                            }
                        }
                    }
                }
            }
        }
    };


    public void writeToFile(String data) {
        try {
            Log.i("MyApp", "writing downloaded to file");

            File file = new File(getFilesDir().getAbsolutePath(), "questions.json");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data.getBytes());
            fos.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
