package com.masjidumar.masjid;

/* Alarm Broadcast Receiver - code that runs when the alarm is triggered (time hits)
 * Contains helper functions to set the next Alarm  and get the next desired alert time (target time)*/

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    public final static String URL_EXTRA = "com.masjidumar.masjid.URL_STRING";
    public final static String CACHEDIR_EXTRA = "com.masjidumar.masjid.CACHEDIR_FILE";
    public final static String ALARMPRAYER_EXTRA = "com.masjidumar.masjid.ALARM_PRAYER";

    public final static int ALARM_ID = 787;
    public final static int notifyID = 878;
    @Override
    public void onReceive(Context context, Intent intent) {
        //extract the extras from the intent
        Bundle extrasBundle = intent.getExtras();
        SharedPreferences sP = PreferenceManager.getDefaultSharedPreferences(context);

        /* Set the device audio state */
        int state = AudioManager.RINGER_MODE_NORMAL;
        String audioState = sP.getString("audio_state", "1");
        if(audioState != null) {
            switch (audioState) {
                case "1":
                    state = AudioManager.RINGER_MODE_NORMAL;
                    audioState = "normal";
                    break;
                case "2":
                    state = AudioManager.RINGER_MODE_VIBRATE;
                    audioState = "vibrate";
                    break;
                case "3":
                    state = AudioManager.RINGER_MODE_SILENT;
                    audioState = "silent";
                    break;
                default:
                    state = AudioManager.RINGER_MODE_NORMAL;
                    audioState = "normal";
                    break;
            }
        }

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(state);
        /* Now make the notification */

        // get the title string
        String contentTitle = context.getString(R.string.jamaat_time);
        if(extrasBundle.containsKey(ALARMPRAYER_EXTRA)){
            contentTitle = context.getString(intent.getIntExtra(ALARMPRAYER_EXTRA, 0))
                    +" " + context.getString(R.string.jamaat_time);
        }

        //Define sound URI
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //make a notification
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.notification_template_icon_bg)
                        .setContentTitle(contentTitle)
                        .setContentText("Get to the Masjid!")
                        .setSubText("Your device is set to " + audioState + " mode.")
                        .setAutoCancel(true);

        if(sP.getBoolean("play_sound", true)){
            mBuilder.setSound(soundUri);
        }

        //Set the notification to open the App when clicked
        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(notifyID, mBuilder.build());

        // set the next alarm
        if(extrasBundle.containsKey(URL_EXTRA) && extrasBundle.containsKey(CACHEDIR_EXTRA)) {
            String urlStr = intent.getStringExtra(URL_EXTRA);
            File cacheDir = (File) intent.getSerializableExtra(CACHEDIR_EXTRA);
            TargetTime targetTime = getTargetTime(context, urlStr, cacheDir);
            setNextAlarm(context, targetTime, urlStr, cacheDir);
        }

    }

    public void setNextAlarm(Context context, TargetTime targetTime, String urlStr, File cacheDir) {
        if(targetTime != null) {
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
            intent.putExtra(URL_EXTRA, urlStr);
            intent.putExtra(CACHEDIR_EXTRA, cacheDir);
            if( targetTime.getID() != -1 ) {
                intent.putExtra(ALARMPRAYER_EXTRA, targetTime.getID());
            }
            PendingIntent pIntent = PendingIntent.getBroadcast(context, ALARM_ID, intent, Intent.FILL_IN_DATA | PendingIntent.FLAG_CANCEL_CURRENT);
            GregorianCalendar targetCal = targetTime.getCal();
            am.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pIntent);
        }
    }

    public TargetTime getTargetTime(Context context, String urlStr, File cacheDir){
        //get preferences
        SharedPreferences sP = PreferenceManager.getDefaultSharedPreferences(context);

        //get today's date and time
        GregorianCalendar gCalToday = new GregorianCalendar();
        //get tomorrow's date
        GregorianCalendar gCalTmrw = new GregorianCalendar();
        gCalTmrw.add(GregorianCalendar.DAY_OF_MONTH, 1);

        HashMap<String, GregorianCalendar> timingsToday = null;
        HashMap<String, GregorianCalendar> timingsTmrw = null;
        String[] pNames = {"fajr", "sunrise", "dhuhr", "asr", "maghrib", "isha"};
        int[] stringIDs = {R.string.fajr, R.string.sunrise, R.string.dhuhr, R.string.asr, R.string.maghrib, R.string.isha};

        // get today's timings
        try{
            timingsToday = new TimingsParser().updateXMLTimings(cacheDir, gCalToday);
        } catch (IOException e){
            try {
                timingsToday = new TimingsParser().downloadXMLTimings(urlStr, cacheDir, gCalToday);
            } catch (IOException ie){
                Log.w("getTargetTimeToday:", ie.getMessage());
            }
        }
        //get tomorrow's timings
        try{
            timingsTmrw = new TimingsParser().updateXMLTimings(cacheDir, gCalTmrw);
        } catch (IOException e){
            try {
                timingsTmrw = new TimingsParser().downloadXMLTimings(urlStr, cacheDir, gCalTmrw);
            } catch (IOException ie){
                Log.w("getTargetTimeTomorrow:", ie.getMessage());
            }
        }

        ArrayList<TargetTime> availableTimings = new ArrayList<TargetTime>(12);
        //modifier how many minutes before the user wants to be reminded (load from preferences)
        int modifier;
        String timeBefore = sP.getString("time_before", "10");
        modifier = -1 * Integer.parseInt(timeBefore);

        //add timings from today to available timings in order
        if(timingsToday != null) {
            for (int i = 0; i < pNames.length; i++) {
                String pName = pNames[i];
                int pID = stringIDs[i];
                GregorianCalendar jCal = timingsToday.get(pName);
                if (jCal != null) {
                    jCal.add(GregorianCalendar.MINUTE, modifier);
                    availableTimings.add(new TargetTime(pName, jCal, pID));
                }
            }
        }
        //add timings from tomorrow to available timings in order
        if(timingsTmrw != null) {
            for (int i = 0; i < pNames.length; i++) {
                String pName = pNames[i];
                int pID = stringIDs[i];
                GregorianCalendar jCal = timingsTmrw.get(pName);
                if (jCal != null) {
                    jCal.add(GregorianCalendar.MINUTE, modifier);
                    availableTimings.add(new TargetTime(pName, jCal, pID));
                }
            }
        }

        //progress to available timings until the next jamaat that needs an alarm is found
        for( int i = 0; i < availableTimings.size(); i++){
            // if gCalToday is before a timing and we are interested in an alarm for it
            if (gCalToday.before(availableTimings.get(i).getCal())) {
                // return this time as the next target time
                return availableTimings.get(i);
            }
        }
        return null;
    }
}
