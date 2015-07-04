package com.masjidumar.masjid;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import java.util.GregorianCalendar;

/**
 * Reverts the audio state of the device after it has been changed by the app
 */
public class RevertStateBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //extract the extras from the intent
        Bundle extrasBundle = intent.getExtras();
        /* Reset the devices audio state */
        if(extrasBundle.containsKey(AlarmBroadcastReceiver.RINGERSTATE_EXTRA)) {
            int audioState = intent.getIntExtra(AlarmBroadcastReceiver.RINGERSTATE_EXTRA, AudioManager.RINGER_MODE_NORMAL);
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            //If the user has not changed the audio state from the state it was before the
            // alarm triggered, then revert it
            if(audioManager.getRingerMode() == audioState) {
                audioManager.setRingerMode(audioState);
            }
        }
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // cancel the ongoing notification
        mNotifyMgr.cancel(AlarmBroadcastReceiver.NOTIFY_ID);
    }
}
