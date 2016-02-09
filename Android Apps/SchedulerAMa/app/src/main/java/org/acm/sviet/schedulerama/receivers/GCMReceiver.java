package org.acm.sviet.schedulerama.receivers;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import org.acm.sviet.schedulerama.services.ScheduleGcmMessageHandler;

/**
 * Created by Anurag on 03-02-2016.
 */
public class GCMReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName comp = new ComponentName(context.getPackageName(), ScheduleGcmMessageHandler.class.getName());

        startWakefulService(context,(intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}