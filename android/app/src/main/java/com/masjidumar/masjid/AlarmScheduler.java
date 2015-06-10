package com.masjidumar.masjid;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * Created by Haidar on 6/10/2015.
 */
public class AlarmScheduler {
    public void setAlarm(String urlStr, File cacheDir){
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
            timingsToday = new TimingsParser().updateXMLTimings(
                    cacheDir,
                    gCalToday.get(GregorianCalendar.YEAR),
                    gCalToday.get(GregorianCalendar.MONTH )+ 1,
                    gCalToday.get(GregorianCalendar.DAY_OF_MONTH));
        } catch (IOException e){
            try {
                timingsToday = new TimingsParser().downloadXMLTimings(
                        urlStr,
                        cacheDir,
                        gCalToday.get(GregorianCalendar.YEAR),
                        gCalToday.get(GregorianCalendar.MONTH) + 1,
                        gCalToday.get(GregorianCalendar.DAY_OF_MONTH));
            } catch (IOException ie){
                ie.printStackTrace();
            }
        }
        //get tomorrow's timings
        try{
            timingsTmrw = new TimingsParser().updateXMLTimings(
                    cacheDir,
                    gCalTmrw.get(GregorianCalendar.YEAR),
                    gCalTmrw.get(GregorianCalendar.MONTH) + 1,
                    gCalTmrw.get(GregorianCalendar.DAY_OF_MONTH));
        } catch (IOException e){
            try {
                timingsTmrw = new TimingsParser().downloadXMLTimings(
                        urlStr,
                        cacheDir,
                        gCalTmrw.get(GregorianCalendar.YEAR),
                        gCalTmrw.get(GregorianCalendar.MONTH) + 1,
                        gCalTmrw.get(GregorianCalendar.DAY_OF_MONTH));
            } catch (IOException ie){
                ie.printStackTrace();
            }
        }

        ArrayList<GregorianCalendar> availableTimings = new ArrayList<GregorianCalendar>();
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
                //TODO set the alarm
                break;
            }
        }












    }
}
