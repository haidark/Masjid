package com.masjidumar.masjid;

import android.app.ActionBar;
import android.app.Activity;
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
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableRow;
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


public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    // Cal with date picked by user to show
    static GregorianCalendar pickedDate;
    UpdateAlarmTask alarmTask;
    UpdateTimingsXMLTask updateTask;
    DownloadTimingsXMLTask downloadTask;

    //prayer names
    String[] pNames = {"fajr", "sunrise", "dhuhr", "asr", "maghrib", "isha"};
    //view IDs
    int[] viewIDs = {R.id.fajrTime, R.id.sunriseTime, R.id.dhuhrTime, R.id.asrTime,
            R.id.maghribTime, R.id.ishaTime};

    int[] stringIDs = {R.string.fajr, R.string.sunrise, R.string.dhuhr, R.string.asr,
            R.string.maghrib, R.string.isha};

    int[] rowIDs = {R.id.fajrRow, R.id.sunriseRow, R.id.dhuhrRow, R.id.asrRow, R.id.maghribRow,
            R.id.ishaRow};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set the picked date to today
        setDateToday();
        //initialize progress dialog
        progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //set the alarm if enabled
        setAlarm();
    }

    @Override
    protected void onStop() {
        super.onStop();

        //cancel all background tasks
        if(alarmTask != null){
            alarmTask.cancel(true);
        }
        if(updateTask != null){
            updateTask.cancel(true);
        }
        if(downloadTask != null){
            downloadTask.cancel(true);
        }
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
            case R.id.action_today:
                setDateToday();
                return true;
            case R.id.action_refresh:
                refreshTimings();
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void refreshTimings(){
        downloadTask = new DownloadTimingsXMLTask();
        downloadTask.execute(pickedDate);
    }

    public void updateTimings(){
        updateTask = new UpdateTimingsXMLTask();
        updateTask.execute(pickedDate);
        //set row colors according to reminder settings for the picked Date
        setRowColors();
    }

    private class UpdateAlarmTask extends AsyncTask<Void, Void, TargetTime>{
        @Override
        protected TargetTime doInBackground(Void... params) {
            String urlStr = getString(R.string.jamaat_URL);
            //Set the Alarm
            AlarmBroadcastReceiver alarmBR = new AlarmBroadcastReceiver();
            //get target time

            TargetTime targetTime = alarmBR.getTargetTime(getApplicationContext(), urlStr, getCacheDir());
            //test Alarm
            //targetTime = new TargetTime("test", new GregorianCalendar());
            // set the next alarm
            alarmBR.setNextAlarm(getApplicationContext(), targetTime, urlStr, getCacheDir());
            // return the target time to update the view
            return targetTime;
        }

        @Override
        protected void onPostExecute(TargetTime targetTime) {
            super.onPostExecute(targetTime);
            //update nextAlarm TextView
            TextView nextAlarm = (TextView) findViewById(R.id.nextAlarm);
            String nextText;
            if(targetTime != null) {
                // Get target time as a string
                GregorianCalendar targetCal = targetTime.getCal();
                SimpleDateFormat format = (SimpleDateFormat) SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT);
                format.setCalendar(targetCal);

                //get Today or Tomorrow
                String relativeTime = (String) DateUtils.getRelativeTimeSpanString(targetCal.getTimeInMillis(),
                        new GregorianCalendar().getTimeInMillis(), DateUtils.DAY_IN_MILLIS);
                //get time string
                String timeString = format.format(targetCal.getTime());
                //get label
                String alarmLabel = " " + getString(R.string._for_) + " " + capFirst(targetTime.getLabel());
                // put the text together
                nextText = getString(R.string.next_alarm) + " " + relativeTime + ", " + timeString + alarmLabel +".";
                //Update view to let the user know an alarm has been set

            } else{
                nextText = getString(R.string.no_alarm);
                //Toast to let the user know the alarm failed to set
                //Toast.makeText(getApplicationContext(), R.string.unable_alarm, Toast.LENGTH_LONG).show();
            }
            nextAlarm.setText(nextText);
        }
    }

    private class UpdateTimingsXMLTask extends AsyncTask<GregorianCalendar, Void, HashMap<String, GregorianCalendar> > {

        @Override
        protected HashMap<String, GregorianCalendar> doInBackground(GregorianCalendar... Params){
            // Uses an instance of TimingsParser to return the saved xml document
            try {
                return new TimingsParser().updateXMLTimings(getCacheDir(), Params[0]);
            } catch(IOException e){
                Log.w("updateTask:", e.getMessage());
                // if we fail to find the saved XML Document, pass the calendar object to PostExecute
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
            progressDialog.setMessage(getString(R.string.downloading_masjid));
            progressDialog.show();
        }

        @Override
        protected HashMap<String, GregorianCalendar> doInBackground(GregorianCalendar... Params){
            // Uses an instance of TimingsParser to download and save the XML file,
            // also returns the document
            String xmlJURL = getString(R.string.jamaat_URL);
            try {
                return new TimingsParser().downloadXMLTimings(xmlJURL, getCacheDir(), Params[0]);
            } catch(IOException e){
                Log.w("DownloadTask:", e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(HashMap<String, GregorianCalendar> timings){
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if(timings != null) {
                displayTimings(timings);
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.download_failed), Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void displayTimings(HashMap<String, GregorianCalendar> timings){
        //format the date
        SimpleDateFormat format = (SimpleDateFormat) SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT);
        GregorianCalendar gCal = pickedDate;
        format.setCalendar(gCal);
        String pickedDateStr = format.format(gCal.getTime());
        TextView pickDateView;
        //display date
        pickDateView = (TextView) findViewById(R.id.pickDateButton);
        pickDateView.setText(pickedDateStr);

        //Only update views if some timings were available
        // if no timings are available, Toast the user to let them know it failed
        boolean noFailure = true;
        if(timings != null) {
            TextView view;
            //SimpleDateFormat format = new SimpleDateFormat("H:mm", Locale.getDefault());
            format = (SimpleDateFormat) SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT);

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
                    view.setText(R.string.empty_time);
                    noFailure = false;
                }
            }
            //if some timings were unavailable, let the user know
            if(!noFailure) {
                Toast.makeText(this, R.string.timings_unavailable, Toast.LENGTH_LONG).show();
            }
            //set the next alarm if enabled
            setAlarm();
        } else{
            Toast.makeText(this, R.string.masjid_unavailable, Toast.LENGTH_LONG).show();
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

    public void toggleRow(View v){
        //Toast.makeText(this, v.getId()+" ", Toast.LENGTH_LONG).show();
    }

    /* Sets the picked Date to today */
    public void setDateToday(){
        pickedDate = new GregorianCalendar();
        updateTimings();
    }

    public void setRowColors(){
        SharedPreferences sP = PreferenceManager.getDefaultSharedPreferences(this);
        // reset every color to inactive
        for( int id : rowIDs){
            TableRow tR = (TableRow) findViewById(id);
            tR.setBackgroundColor(getResources().getColor(R.color.inactive));
        }

        // Only color rows if the user has reminders enabled
        if(sP.getBoolean(SettingsActivity.SettingsFragment.STATE_ENABLE_KEY, true)) {
            //first check day of the week
            int weekDay = pickedDate.get(GregorianCalendar.DAY_OF_WEEK);
            //get the days of the week the user wants to be reminded for
            String daysOfWeek = sP.getString(SettingsActivity.SettingsFragment.WEEKDAY_SELECTION_KEY,
                    getString(R.string.weekday_default));
            // SUNDAY == 1 -- SATURDAY == 7
            if(daysOfWeek.charAt(weekDay-1) == '1'){
                //get the prayers of the day the user wants to be reminded for
                String prayerOfDay = sP.getString(SettingsActivity.SettingsFragment.PRAYER_SELECTION_KEY,
                        getString(R.string.prayer_sel_default));
                for(int i = 0; i < rowIDs.length; i++){
                    if(prayerOfDay.charAt(i) == '1'){
                        TableRow tR = (TableRow) findViewById(rowIDs[i]);
                        tR.setBackgroundColor(getResources().getColor(R.color.active));
                    }
                }
            }

        }
    }

    public void setAlarm(){
        SharedPreferences sP = PreferenceManager.getDefaultSharedPreferences(this);
        if(sP.getBoolean(SettingsActivity.SettingsFragment.STATE_ENABLE_KEY, true)) {
            //set the next alarm in the background
            alarmTask = new UpdateAlarmTask();
            alarmTask.execute();
        } else {
            //Cancel the Alarm
            AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmBroadcastReceiver.class);
            PendingIntent pIntent = PendingIntent.getBroadcast(this, AlarmBroadcastReceiver.ALARM_ID, intent, Intent.FILL_IN_DATA | PendingIntent.FLAG_CANCEL_CURRENT);
            am.cancel(pIntent);
            //Update view to let the user know no alarm has been set
            String nextText = getString(R.string.no_alarm);
            TextView nextAlarm = (TextView) findViewById(R.id.nextAlarm);
            nextAlarm.setText(nextText);
        }
    }

    /* function to capitalize first letter of a String */
    public String capFirst(String original){
        if(original.length() == 0)
            return original;
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }
}
