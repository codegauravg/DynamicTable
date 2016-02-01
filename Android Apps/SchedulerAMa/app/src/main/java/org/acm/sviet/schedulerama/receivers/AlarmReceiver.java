package org.acm.sviet.schedulerama.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.acm.sviet.schedulerama.services.BackgroundService;

/**
 * Created by Anurag on 31-01-2016.
 *
 * A Broadcast Alarm Receiver created to start Broadcast Service in background.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context,BackgroundService.class);
        context.startService(service);
    }
}
