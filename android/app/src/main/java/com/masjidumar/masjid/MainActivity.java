package com.masjidumar.masjid;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class MainActivity extends ActionBarActivity {

    private ProgressDialog progressDialog;
    // Cal with date picked by user to show
    static GregorianCalendar pickedDate;

    //prayer names
    String[] pNames = {"fajr", "sunrise", "dhuhr", "asr", "maghrib", "isha"};
    //view IDs
    int[] viewIDs = {R.id.fajrTime, R.id.sunriseTime, R.id.dhuhrTime, R.id.asrTime,
            R.id.maghribTime, R.id.ishaTime};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //try to load timings from the previously downloaded file
        setDateToday();

        //initialize progress dialog
        progressDialog = new ProgressDialog(this);

        //set the next alarm in the background
        new updateAlarmTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_refresh:
                refreshTimings();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void refreshTimings(){
        new DownloadTimingsXMLTask().execute(pickedDate);
    }

    public void updateTimings(){
        new UpdateTimingsXMLTask().execute(pickedDate);
    }

    private class updateAlarmTask extends AsyncTask<Void, Void, TargetTime>{
        @Override
        protected TargetTime doInBackground(Void... params) {
            String urlStr = getString(R.string.jamaat_URL);
            //Set the Alarm
            AlarmBroadcastReceiver alarmBR = new AlarmBroadcastReceiver();
            //get target time

            TargetTime targetTime = alarmBR.getTargetTime(urlStr, getCacheDir());
            // set the next alarm
            alarmBR.setNextAlarm(getApplicationContext(), targetTime, urlStr, getCacheDir());
            // return the target time to update the view
            return targetTime;
        }

        @Override
        protected void onPostExecute(TargetTime targetTime) {
            super.onPostExecute(targetTime);
            //update nextAlarm TextView
            if(targetTime != null) {
                // Get target time as a string
                GregorianCalendar targetCal = targetTime.getCal();
                SimpleDateFormat format = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT);
                format.setCalendar(targetCal);
                String nextText = "Next alarm on: " + format.format(targetCal.getTime())
                        + " for "+capFirst(targetTime.getLabel())+".";
                //Update view to let the user know an alarm has been set
                TextView nextAlarm = (TextView) findViewById(R.id.nextAlarm);
                nextAlarm.setText(nextText);
            }
        }
    }

    private class UpdateTimingsXMLTask extends AsyncTask<GregorianCalendar, Void, HashMap<String, GregorianCalendar> > {

        @Override
        protected HashMap<String, GregorianCalendar> doInBackground(GregorianCalendar... Params){
            // Uses an instance of TimingsParser to return the saved xml document
            try {
                return new TimingsParser().updateXMLTimings(getCacheDir(), Params[0]);
            } catch(IOException e){
                e.printStackTrace();
                // if we fail to find the saved XML Document, pass the year, month, and day to PostExecute
                HashMap<String, GregorianCalendar> timings = new HashMap<String, GregorianCalendar>();
                timings.put("fileNotFound", Params[0]);
                return timings;
            }
        }

        @Override
        protected void onPostExecute(HashMap<String, GregorianCalendar> timings){
            // if file was not found, then pickedDate will be set
            if(timings.containsKey("fileNotFound")) {
                new DownloadTimingsXMLTask().execute(timings.get("fileNotFound"));
            } else{
                displayTimings(timings);
            }
        }
    }

    private class DownloadTimingsXMLTask extends AsyncTask<GregorianCalendar, Void, HashMap<String, GregorianCalendar> > {

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Downloading file...");
            //progressDialog.show();
        }

        @Override
        protected HashMap<String, GregorianCalendar> doInBackground(GregorianCalendar... Params){
            // Uses an instance of TimingsParser to download and save the XML file,
            // also returns the document
            String xmlJURL = getString(R.string.jamaat_URL);
            try {
                return new TimingsParser().downloadXMLTimings(xmlJURL, getCacheDir(), Params[0]);
            } catch(IOException e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(HashMap<String, GregorianCalendar> timings){
            displayTimings(timings);
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }
    }

    public void displayTimings(HashMap<String, GregorianCalendar> timings){
        //Only update views if some timings were available
        // if no timings are available, Toast the user to let them know it failed
        if(timings != null) {
            TextView view;
            //SimpleDateFormat format = new SimpleDateFormat("H:mm", Locale.getDefault());
            GregorianCalendar gCal;
            SimpleDateFormat format = (SimpleDateFormat) SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT);

            //get the available timings for this date and display them
            // if a timing is not available, blank it out.
            for (int i = 0; i < pNames.length; i++) {
                //get view
                view = (TextView) findViewById(viewIDs[i]);
                gCal = timings.get(pNames[i]);
                if (gCal != null) {
                    format.setCalendar(gCal);
                    view.setText(format.format(gCal.getTime()));
                } else {
                    view.setText("--:--:-- --");
                }
            }

            //format the date
            format = (SimpleDateFormat) SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG);
            gCal = pickedDate;
            format.setCalendar(gCal);
            String pickedDateStr = format.format(gCal.getTime());

            //display date
            view = (TextView) findViewById(R.id.pickedDate);
            view.setText(pickedDateStr);
        } else{
            Toast.makeText(this, "Unable to load timings for this masjid.", Toast.LENGTH_LONG).show();
        }
    }

    /* Date Picker Fragment */
    public static class DatePickerFragment extends DialogFragment{

        private DatePickerDialog.OnDateSetListener onDateSetListener;

        static DatePickerFragment newInstance(DatePickerDialog.OnDateSetListener onDateSetListener) {
            DatePickerFragment pickerFragment = new DatePickerFragment();
            pickerFragment.setOnDateSetListener(onDateSetListener);

            return pickerFragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            super.onCreateDialog(savedInstanceState);

            //get the year, month, and day from the pickedDate
            int year = pickedDate.get(GregorianCalendar.YEAR);
            int month = pickedDate.get(GregorianCalendar.MONTH);
            int day = pickedDate.get(GregorianCalendar.DAY_OF_MONTH);

            // Use the current set date as the default date in the picker
            return new DatePickerDialog(getActivity(), onDateSetListener, year, month, day);
        }

        private void setOnDateSetListener(DatePickerDialog.OnDateSetListener listener) {
            this.onDateSetListener = listener;
        }
    }

    /* Listener for onDateSet */
    public DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int pickedYear, int pickedMonth, int pickedDay) {
            //update the pickedDate variable
            pickedDate.set(pickedYear, pickedMonth, pickedDay);
            updateTimings();
        }
    };
    /* On-Click function for picking a date, creates a datepicker fragement */
    public void pickDate(View v){
        DialogFragment newFragment = DatePickerFragment.newInstance(onDateSetListener);
        newFragment.show(getFragmentManager(), "DayDatePicker");
    }
    /* On-click function for resetting date to today */
    public void setDateTodayCallback(View v) {
        setDateToday();
    }

    public void setDateToday(){
        pickedDate = new GregorianCalendar();
        updateTimings();
    }

    /* function to capitalize first letter of a String */
    public String capFirst(String original){
        if(original.length() == 0)
            return original;
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }
}
