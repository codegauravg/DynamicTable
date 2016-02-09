package org.acm.sviet.schedulerama.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.acm.sviet.schedulerama.R;
import org.acm.sviet.schedulerama.ScheduleViewActivity;
import org.acm.sviet.schedulerama.receivers.GCMReceiver;

/**
 * Created by Anurag on 03-02-2016.
 */
public class ScheduleGcmMessageHandler extends IntentService {
    String TAG="/GcmMessageHandler";
    String msg,title;
    private Handler handler;
    NotificationCompat.Builder mBuilder ;

    public ScheduleGcmMessageHandler() {
        super("ScheduleGcmMessageHandler");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String msgType = gcm.getMessageType(intent);

        msg = extras.getString("message");
        title = extras.getString("title");
        showToast();
        Log.e(TAG, "Recieved :(" + msgType + ") " + extras.getString("title"));
        GCMReceiver.completeWakefulIntent(intent);
    }

    private void showToast() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                mBuilder =
                        new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(title)
                                .setTicker(msg)
                                .setContentText(msg)
                                .setContentIntent(PendingIntent.getActivity(
                                        ScheduleGcmMessageHandler.this,
                                        0,
                                        new Intent(ScheduleGcmMessageHandler.this, ScheduleViewActivity.class),
                                        PendingIntent.FLAG_UPDATE_CURRENT
                                ))
                                .setAutoCancel(true)
                                .setDefaults(Notification.DEFAULT_ALL);
                // Builds the notification and issues it.
                mNotifyMgr.notify(01, mBuilder.build());
            }
        });


    }
}
