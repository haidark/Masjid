package com.masjidumar.masjid;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

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
            audioManager.setRingerMode(audioState);
            /* Update the notification to indicate the state has been changed */
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.notification_template_icon_bg)
                            .setContentTitle("End Jamaat Time")
                            .setContentText("Get out of the Masjid!")
                            .setSubText("Your device's audio state has been reset.")
                            .setAutoCancel(true);

            // Gets an instance of the NotificationManager service
            NotificationManager mNotifyMgr =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // Builds the notification and issues it.
            mNotifyMgr.notify(AlarmBroadcastReceiver.NOTIFY_ID, mBuilder.build());
        }
    }
}
