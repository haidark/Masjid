package com.masjidumar.masjid;

/* Broadcast Receiver - code that runs when the alarm is triggered (time hits) */

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
    public final static int ALARM_ID = 787;
    @Override
    public void onReceive(Context context, Intent intent) {
        //make a notification
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.notification_template_icon_bg)
                        .setContentTitle("Its Prayer Time!")
                        .setContentText("Get to the Masjid baddie!");

        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(1, mBuilder.build());

        //extract the extras from the intent and set the next alarm
        Bundle extrasBundle = intent.getExtras();
        if(extrasBundle.containsKey(URL_EXTRA) && extrasBundle.containsKey(CACHEDIR_EXTRA)) {
            String urlStr = intent.getStringExtra(URL_EXTRA);
            File cacheDir = (File) intent.getSerializableExtra(CACHEDIR_EXTRA);
            TargetTime targetTime = getTargetTime(urlStr, cacheDir);
            setNextAlarm(context, targetTime, urlStr, cacheDir);
        }

    }

    public void setNextAlarm(Context context, TargetTime targetTime, String urlStr, File cacheDir) {
        if(targetTime != null) {
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
            intent.putExtra(URL_EXTRA, urlStr);
            intent.putExtra(CACHEDIR_EXTRA, cacheDir);
            PendingIntent pIntent = PendingIntent.getBroadcast(context, ALARM_ID, intent, 0);
            GregorianCalendar targetCal = targetTime.getCal();
            am.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pIntent);
        }
    }

    public TargetTime getTargetTime(String urlStr, File cacheDir){
        //get today's date and time
        GregorianCalendar gCalToday = new GregorianCalendar();
        //get tomorrow's date
        GregorianCalendar gCalTmrw = new GregorianCalendar();
        gCalTmrw.add(GregorianCalendar.DAY_OF_MONTH, 1);

        HashMap<String, GregorianCalendar> timingsToday = null;
        HashMap<String, GregorianCalendar> timingsTmrw = null;
        String[] pNames = {"fajr", "sunrise", "dhuhr", "asr", "maghrib", "isha"};
        // get today's timings
        try{
            timingsToday = new TimingsParser().updateXMLTimings(cacheDir, gCalToday);
        } catch (IOException e){
            try {
                timingsToday = new TimingsParser().downloadXMLTimings(urlStr, cacheDir, gCalToday);
            } catch (IOException ie){
                ie.printStackTrace();
            }
        }
        //get tomorrow's timings
        try{
            timingsTmrw = new TimingsParser().updateXMLTimings(cacheDir, gCalTmrw);
        } catch (IOException e){
            try {
                timingsTmrw = new TimingsParser().downloadXMLTimings(urlStr, cacheDir, gCalTmrw);
            } catch (IOException ie){
                ie.printStackTrace();
            }
        }

        ArrayList<TargetTime> availableTimings = new ArrayList<TargetTime>(12);
        //modifier how many minutes before the user wants to be reminded
        int modifier = -10;
        //add timings from today to available timings in order
        if(timingsToday != null) {
            for (String pName : pNames) {
                GregorianCalendar jCal = timingsToday.get(pName);
                if (jCal != null) {
                    jCal.add(GregorianCalendar.MINUTE, modifier);
                    availableTimings.add(new TargetTime(pName, jCal));
                }
            }
        }
        //add timings from tomorrow to available timings in order
        if(timingsTmrw != null) {
            for (String pName : pNames) {
                GregorianCalendar jCal = timingsTmrw.get(pName);
                if (jCal != null) {
                    jCal.add(GregorianCalendar.MINUTE, modifier);
                    availableTimings.add(new TargetTime(pName, jCal));
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
