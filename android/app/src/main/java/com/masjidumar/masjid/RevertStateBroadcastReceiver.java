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
        if(extrasBundle.containsKey(AlarmBroadcastReceiver.RINGERSTATE_EXTRA) &&
                extrasBundle.containsKey(AlarmBroadcastReceiver.SETSTATE_EXTRA)) {
            int prevAudioState = intent.getIntExtra(AlarmBroadcastReceiver.RINGERSTATE_EXTRA, AudioManager.RINGER_MODE_NORMAL);
            int setAudioState = intent.getIntExtra(AlarmBroadcastReceiver.SETSTATE_EXTRA, AudioManager.RINGER_MODE_NORMAL);
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            //If the user has not changed the audio state from the state it was before the
            // alarm triggered, then revert it
            if(audioManager.getRingerMode() == setAudioState) {
                audioManager.setRingerMode(prevAudioState);
            }
        }
        String notifTitle = context.getString(R.string.notification_title);
        String notifText = context.getString(R.string.notification_end_text);

        //make a notification
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(notifTitle)
                        .setContentText(notifText);

        //Set the notification to open the App when clicked
        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // override old notification or create a new one to let user know audio state has been reverted
        mNotifyMgr.notify(AlarmBroadcastReceiver.NOTIFY_ID, mBuilder.build());
    }
}
