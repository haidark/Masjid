package com.masjidumar.masjid;

/* Alarm Broadcast Receiver - code that runs when the alarm is triggered (time hits)
 * Contains helper functions to set the next Alarm  and get the next desired alert time (target time)
 * also has a function to schedule teh revert state alarm */

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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    public final static String URL_EXTRA = "com.masjidumar.masjid.URL_STRING";
    public final static String CACHEDIR_EXTRA = "com.masjidumar.masjid.CACHEDIR_FILE";
    public final static String ALARMPRAYER_EXTRA = "com.masjidumar.masjid.ALARM_PRAYER";
    public final static String RINGERSTATE_EXTRA = "com.masjidumar.masjid.RINGER_STATE";
    public final static String SETSTATE_EXTRA = "com.masjidumar.masjid.SET_STATE";

    public final static int ALARM_ID = 787;
    public final static int REVERT_ALARM_ID = 778;

    public final static int NOTIFY_ID = 878;

    @Override
    public void onReceive(Context context, Intent intent) {
        //extract the extras from the intent
        Bundle extrasBundle = intent.getExtras();
        SharedPreferences sP = PreferenceManager.getDefaultSharedPreferences(context);

        // Only take action if the user has reminders enabled
        if(sP.getBoolean(SettingsActivity.SettingsFragment.STATE_ENABLE_KEY, true)) {

            /* Get the device's current audio state */
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            int prevAudioState = audioManager.getRingerMode();

            /* Set the device audio state to new state */
            int setAudioState = AudioManager.RINGER_MODE_NORMAL;
            String audioState = sP.getString(SettingsActivity.SettingsFragment.AUDIO_STATE_KEY,
                    context.getString(R.string.audio_state_default));
            String[] stateStrings = context.getResources().getStringArray(R.array.audio_state_entries);
            if (audioState != null) {
                //Silent = 0
                //Vibrate = 1
                //Normal = 2
                for(int i = 0; i < stateStrings.length; i++){
                    if(audioState.equals(stateStrings[i])){
                        setAudioState = i;
                        break;
                    }
                }
            }
            audioManager.setRingerMode(setAudioState);

            /* set the alarm to revert the audio state after some time */
            setRevertAlarm(context, prevAudioState, setAudioState);

            /* Now make the notification */
            // get the title string
            String notifTitle = context.getString(R.string.notification_title);
            if (extrasBundle.containsKey(ALARMPRAYER_EXTRA)) {
                notifTitle = intent.getStringExtra(ALARMPRAYER_EXTRA)
                        + " " + notifTitle;
            }
            // get the text string
            String notifText = context.getString(R.string.notification_start_text);
            notifText = notifText +": "+ audioState;

            //Define sound URI
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            //make a notification
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(notifTitle)
                            .setContentText(notifText)
                            .setSound(soundUri);

            //Set the notification to open the App when clicked
            Intent resultIntent = new Intent(context, MainActivity.class);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);

            // Gets an instance of the NotificationManager service
            NotificationManager mNotifyMgr =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // Builds the notification and issues it.
            mNotifyMgr.notify(NOTIFY_ID, mBuilder.build());

            // set the next alarm if the necessary data was provided and alarm is enabled
            if (extrasBundle.containsKey(URL_EXTRA) && extrasBundle.containsKey(CACHEDIR_EXTRA) &&
                    sP.getBoolean(SettingsActivity.SettingsFragment.STATE_ENABLE_KEY, true)) {
                String urlStr = intent.getStringExtra(URL_EXTRA);
                File cacheDir = (File) intent.getSerializableExtra(CACHEDIR_EXTRA);
                TargetTime targetTime = getTargetTime(context, urlStr, cacheDir);
                setNextAlarm(context, targetTime, urlStr, cacheDir);
            }
        }
    }

    public void setNextAlarm(Context context, TargetTime targetTime, String urlStr, File cacheDir) {
        if(targetTime != null) {
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
            intent.putExtra(URL_EXTRA, urlStr);
            intent.putExtra(CACHEDIR_EXTRA, cacheDir);
            intent.putExtra(ALARMPRAYER_EXTRA, targetTime.getLabel());
            PendingIntent pIntent = PendingIntent.getBroadcast(context, ALARM_ID, intent, Intent.FILL_IN_DATA | PendingIntent.FLAG_CANCEL_CURRENT);
            GregorianCalendar targetCal = targetTime.getCal();
            am.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pIntent);
        }
    }

    private void setRevertAlarm(Context context, int prevAudioState, int setAudioState){
        //get preferences
        SharedPreferences sP = PreferenceManager.getDefaultSharedPreferences(context);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, RevertStateBroadcastReceiver.class);
        intent.putExtra(RINGERSTATE_EXTRA, prevAudioState);
        intent.putExtra(SETSTATE_EXTRA, setAudioState);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, REVERT_ALARM_ID, intent, Intent.FILL_IN_DATA | PendingIntent.FLAG_CANCEL_CURRENT);

        //get current time
        GregorianCalendar gCal = new GregorianCalendar();

        //get time before iqamah and duration
        String before = sP.getString(SettingsActivity.SettingsFragment.TIME_BEFORE_KEY, context.getString(R.string.time_before_default));
        String duration = sP.getString(SettingsActivity.SettingsFragment.TIME_DURATION_KEY, context.getString(R.string.time_duration_default));

        // add time before and duration to current time
        int modifier = Integer.parseInt(duration) + Integer.parseInt(before);
        gCal.add(GregorianCalendar.MINUTE, modifier);
        am.set(AlarmManager.RTC_WAKEUP, gCal.getTimeInMillis(), pIntent);
    }

    public TargetTime getTargetTime(Context context, String urlStr, File cacheDir){
        String[] pNames = {"fajr", "sunrise", "dhuhr", "asr", "maghrib", "isha"};
        int[] stringIDs = {R.string.fajr, R.string.sunrise, R.string.dhuhr, R.string.asr, R.string.maghrib, R.string.isha};

        int modifier;
        int daysAhead = 7;

        GregorianCalendar gCalNow = new GregorianCalendar();

        ArrayList<GregorianCalendar> gCalArray = new ArrayList<>(daysAhead);
        ArrayList<TargetTime> availableTimings = new ArrayList<TargetTime>(daysAhead*6);

        //get preferences
        SharedPreferences sP = PreferenceManager.getDefaultSharedPreferences(context);

        //modifier how many minutes before the user wants to be reminded (load from preferences)
        String timeBefore = sP.getString(SettingsActivity.SettingsFragment.TIME_BEFORE_KEY,
                context.getString(R.string.time_before_default));
        modifier = -1 * Integer.parseInt(timeBefore);

        //get the days of the week the user wants to be reminded for
        String daysOfWeek = sP.getString(SettingsActivity.SettingsFragment.WEEKDAY_SELECTION_KEY,
                context.getString(R.string.weekday_default));
        // SUNDAY == 1 -- SATURDAY == 7

        //get the prayers of the day the user wants to be reminded for
        String prayerOfDay = sP.getString(SettingsActivity.SettingsFragment.PRAYER_SELECTION_KEY,
                context.getString(R.string.prayer_sel_default));

        // get date objects for the days ahead
        // only add it to the arraylist if the user has enabled reminders for that day
        for(int i = 0; i < daysAhead; i++){
            GregorianCalendar gCal = new GregorianCalendar();
            gCal.add(GregorianCalendar.DAY_OF_MONTH, i);
            int dayIndex = gCal.get(GregorianCalendar.DAY_OF_WEEK)-1;
            if(daysOfWeek.charAt(dayIndex) == '1') {
                gCalArray.add(gCal);
            }
        }

        // get timings for each day ahead and add each non-null timing to available timings
        for(int i = 0; i < gCalArray.size(); i++){
            HashMap<String, GregorianCalendar> timing = null;
            try{
                timing = new TimingsParser().updateXMLTimings(cacheDir, gCalArray.get(i));
            } catch (IOException e){
                try {
                    timing = new TimingsParser().downloadXMLTimings(urlStr, cacheDir, gCalArray.get(i));
                } catch (IOException ie){
                    Log.w("getTargetTimeToday:", ie.getMessage());
                }
            }
            //if not null
            if(timing != null){
                for (int p = 0; p < pNames.length; p++) {
                    // only add to available timings if the user wants to be reminded for that prayer
                    if(prayerOfDay.charAt(p) == '1') {
                        String pName = pNames[p];
                        int pID = stringIDs[p];
                        GregorianCalendar jCal = timing.get(pName);
                        //if the timing was available
                        if (jCal != null) {
                            jCal.add(GregorianCalendar.MINUTE, modifier);
                            availableTimings.add(new TargetTime(context.getString(pID), jCal));
                        }
                    }
                }
            }
        }

        //progress to available timings until the next jamaat that needs an alarm is found
        for( int i = 0; i < availableTimings.size(); i++){
            // if gCalToday is before a timing and we are interested in an alarm for it
            if (gCalNow.before(availableTimings.get(i).getCal())) {
                // return this time as the next target time
                return availableTimings.get(i);
            }
        }
        return null;
    }
}
