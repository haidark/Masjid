package com.masjidumar.masjid;

/* Broadcast Receiver - code that runs when the alarm is triggered (time hits) */

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    static int once = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm Triggered", Toast.LENGTH_SHORT).show();
        //make a notification
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.notification_template_icon_bg)
                        .setContentTitle("Its Prayer Time")
                        .setContentText("Get to the masjid baddie!");
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(1, mBuilder.build());

        /*if(once == 0) {
            setNextAlarm(context, getTargetTime("",new File("")));
            once = 1;
        }*/

    }

    public void setNextAlarm(Context context, GregorianCalendar targetTime) {
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, targetTime.getTimeInMillis(), pIntent);
    }

    public GregorianCalendar getTargetTime(String urlStr, File cacheDir){
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

        ArrayList<GregorianCalendar> availableTimings = new ArrayList<GregorianCalendar>(10);
        //add timings from today to available timings in order
        if(timingsToday != null) {
            for (String pName : pNames) {
                GregorianCalendar jCal = timingsToday.get(pName);
                if (jCal != null) {
                    availableTimings.add(jCal);
                }
            }
        }
        //add timings from tomorrow to available timings in order
        if(timingsTmrw != null) {
            for (String pName : pNames) {
                GregorianCalendar jCal = timingsTmrw.get(pName);
                if (jCal != null) {
                    availableTimings.add(jCal);
                }
            }
        }

        //progress to available timings until the next jamaat that needs an alarm is found
        for( int i = 0; i < availableTimings.size(); i++){
            // if gCalToday is before a timing and we are interested in an alarm for it
            if (gCalToday.before(availableTimings.get(i))) {
                // return this time as the next target time
                availableTimings.get(i).add(GregorianCalendar.MINUTE, -10);
                return availableTimings.get(i);
            }
        }
        return null;
    }
}
