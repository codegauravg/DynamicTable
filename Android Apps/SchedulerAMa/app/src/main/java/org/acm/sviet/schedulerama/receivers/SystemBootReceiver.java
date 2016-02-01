package org.acm.sviet.schedulerama.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.acm.sviet.schedulerama.services.BackgroundService;

/**
 * Created by Anurag on 27-01-2016.
 *
 * System Boot Intent Receiver.
 */
public class SystemBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //for setting an alarm at system boot. if system is booted intent is forwarded.
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.

            Intent background = new Intent(context, BackgroundService.class);
            context.startService(background);

        }

    }
}
