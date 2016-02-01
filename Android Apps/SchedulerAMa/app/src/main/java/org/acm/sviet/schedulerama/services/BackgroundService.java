package org.acm.sviet.schedulerama.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.acm.sviet.schedulerama.R;
import org.acm.sviet.schedulerama.ScheduleViewActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Anurag on 31-01-2016.
 *
 * A Background Service called by AlarmReceivers to check if any new modification has occurred in the schedule of selected DSS schedule.
 */
public class BackgroundService extends Service {

    private String TAG = "[BackgroundService]";
    private boolean isRunning;
    private Context context;
    private Thread backgroundThread;
    NotificationCompat.Builder mBuilder ;
    Intent scheduleIntent;
    int notId = 01;

    // Because clicking the notification opens a new ("special") activity, there's
// no need to create an artificial back stack.
    PendingIntent schedulePendingIntent;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        this.context = this;
        this.isRunning = false;
        this.backgroundThread = new Thread(myTask);
    }

    private Runnable myTask = new Runnable() {
        public void run() {
            // Do something here
            Log.d(TAG, "Started a Background Service");

            stopSelf();
        }
    };

    @Override
    public void onDestroy() {
        this.isRunning = false;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doConnect();
            }
        }, 1000);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!this.isRunning) {
            this.isRunning = true;
            this.backgroundThread.start();
        }
        return START_STICKY;
    }


    private void doConnect() {
// Make RESTful webservice call using AsyncHttpClient object

        final AsyncHttpClient client = new AsyncHttpClient();

        SharedPreferences preferences;
        preferences = getSharedPreferences("dept_sem_sec_values", MODE_PRIVATE);

        int dept_index = preferences.getInt(getResources().getString(R.string.dept_value_key), getResources().getInteger(R.integer.dept_value_default));
        int sem_index = preferences.getInt(getResources().getString(R.string.sem_value_key), getResources().getInteger(R.integer.sem_value_default));
        int section_index = preferences.getInt(getResources().getString(R.string.section_value_key), getResources().getInteger(R.integer.section_value_default));

        String dept = getResources().getStringArray(R.array.dept_array)[dept_index];
        String sem = getResources().getStringArray(R.array.sem_array)[sem_index];
        String section = getResources().getStringArray(R.array.section_array)[section_index];

        client.get("http://schedulerama-svietacm.rhcloud.com/API/notify/"+dept+"/"+sem+"/"+section, null, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.v(TAG, "Successfull Response Gathering.");

                try {
                    String response = new String(responseBody, "UTF-8");

                    JSONObject obj = new JSONObject(response);

                    int count = obj.getInt("modifiedCount");
                    SharedPreferences sharedPreferences = getSharedPreferences("modification_counter_prefrence", Context.MODE_PRIVATE);
                    Log.d(TAG, String.valueOf(sharedPreferences.getInt(getResources().getString(R.string.modify_count_key),getResources().getInteger(R.integer.modify_count_default))));
                    if(count>sharedPreferences.getInt(getResources().getString(R.string.modify_count_key),getResources().getInteger(R.integer.modify_count_default))){
                        Log.d(TAG,"Schedule for preference DSS has changed.");
                        //Add a NotificationUtil here to pop at feed count updates.
                        // Gets an instance of the NotificationManager service

                        scheduleIntent = new Intent(BackgroundService.this, ScheduleViewActivity.class);
                        schedulePendingIntent =
                                PendingIntent.getActivity(
                                        BackgroundService.this,
                                        0,
                                        scheduleIntent,
                                        PendingIntent.FLAG_UPDATE_CURRENT
                                );

                        NotificationManager mNotifyMgr =
                                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                        mBuilder =
                                new NotificationCompat.Builder(context)
                                        .setSmallIcon(R.mipmap.ic_launcher)
                                        .setContentTitle("Schedulerama")
                                        .setTicker("Schedule is Changed.")
                                        .setContentText("Some Changes have been made in your Schedule.")
                                        .setContentIntent(schedulePendingIntent)
                                        .setAutoCancel(true)
                                        .setDefaults(Notification.DEFAULT_ALL);
                        // Builds the notification and issues it.
                        mNotifyMgr.notify(notId, mBuilder.build());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt(getResources().getString(R.string.modify_count_key),count).apply();
                    }

                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.v(TAG, "Unsuccessful Response Gathering.");

                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occurred! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}