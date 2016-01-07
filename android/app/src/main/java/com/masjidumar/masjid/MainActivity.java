package com.masjidumar.masjid;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    // Cal with date picked by user to show
    static GregorianCalendar pickedDate;
    UpdateAlarmTask alarmTask;
    UpdateTimingsXMLTask updateXMLTask;
    DownloadTimingsXMLTask downloadXMLTask;
    DownloadNewsTask downloadNewsTask;

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
        today_button();
        //initialize progress dialog
        progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //set the alarm if enabled
        setAlarm();
        setNews();
    }

    @Override
    protected void onStop() {
        super.onStop();

        //cancel all background tasks
        if(alarmTask != null){
            alarmTask.cancel(true);
        }
        if(updateXMLTask != null){
            updateXMLTask.cancel(true);
        }
        if(downloadXMLTask != null){
            downloadXMLTask.cancel(true);
        }
        if( downloadNewsTask != null){
            downloadNewsTask.cancel(true);
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
                today_button();
                return true;
            case R.id.action_refresh:
                refresh_button();
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void refresh_button(){
        downloadXMLTask = new DownloadTimingsXMLTask();
        downloadXMLTask.execute(pickedDate);
        //redownload the latest news from the website
        setNews();
    }

    public void updateTimings(){
        updateXMLTask = new UpdateTimingsXMLTask();
        updateXMLTask.execute(pickedDate);
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
                Log.w("updateXMLTask:", e.getMessage());
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
        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_button));
        DialogFragment newFragment = DatePickerFragment.newInstance(onDateSetListener);
        newFragment.show(getFragmentManager(), "DayDatePicker");
    }

    public void toggleRow(View v){
        //Toast.makeText(this, v.getId()+" ", Toast.LENGTH_LONG).show();
    }

    /* Sets the picked Date to today */
    public void today_button(){
        pickedDate = new GregorianCalendar();
        updateTimings();
    }

    public void setRowColors(){
        SharedPreferences sP = PreferenceManager.getDefaultSharedPreferences(this);
        // reset every color to inactive
        LinearLayout tR;
        for( int id : rowIDs){
            tR = (LinearLayout) findViewById(id);
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
                        tR = (LinearLayout) findViewById(rowIDs[i]);
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

    public void setNews(){
        try {
            URL url = new URL(getString(R.string.news_URL));
            downloadNewsTask = new DownloadNewsTask();
            downloadNewsTask.execute(url);
        } catch( MalformedURLException e){
            e.printStackTrace();
        }
    }

    private class DownloadNewsTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls){
            InputStream input = null;
            String fileText = null;
            HttpURLConnection connection = null;
            try {
                URL url = urls[0];
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }


                // download the file
                input = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                fileText = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                try {
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return fileText;
        }

        @Override
        protected void onPostExecute(String fileText){
            if( fileText != null) {
                String lines[] = fileText.split("\\r?\\n");
                String date = lines[0];
                String title = lines[1];
                String text = lines[2];

                TextView newsTitle = (TextView) findViewById(R.id.newsTitle);
                TextView newsDate = (TextView) findViewById(R.id.newsDate);
                TextView newsText = (TextView) findViewById(R.id.newsText);
                newsTitle.setText(title);
                newsDate.setText(date);
                newsText.setText(text);
            }
        }
    }

    /* function to capitalize first letter of a String */
    public String capFirst(String original){
        if(original.length() == 0)
            return original;
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }
}
