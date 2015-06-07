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
    public final static String EXTRA_MESSAGE="com.masjidumar.masjid.MESSAGE";
    ProgressDialog pDialog;
    static int month;
    static int day;
    static int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //try to load timings from the previously downloaded file
        Calendar cal = new GregorianCalendar();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;    //switches from zero-indexed to the sane way
        day = cal.get(Calendar.DAY_OF_MONTH);

        TextView datepicked = (TextView) findViewById(R.id.pickedDate);
        datepicked.setText(month + "/" + day);
        updateTimings();
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
        new DownloadTimingsXMLTask().execute();
    }

    public void updateTimings(){
        new UpdateTimingsXMLTask().execute();
    }

    private class UpdateTimingsXMLTask extends AsyncTask<Void, Void, Document> {

        @Override
        protected Document doInBackground(Void... Params){
            // Uses an instance of TimingsParser to return the saved xml document
            return new TimingsParser().updateXMLTimings(getCacheDir(), year, month);
        }

        @Override
        protected void onPostExecute(Document doc){
            if(doc != null) {
                displayTimings(new TimingsParser().extractTimings(doc, year, month, day));
            } else{
                new DownloadTimingsXMLTask().execute();
            }
        }
    }

    private class DownloadTimingsXMLTask extends AsyncTask<Void, Void, Document> {
        @Override
        protected Document doInBackground(Void... Params){
            // Uses an instance of TimingsParser to download and save the XML file,
            // also returns the document
            String xmlJURL = getString(R.string.jamaat_URL);
            return new TimingsParser().downloadXMLTimings(xmlJURL, getCacheDir(), year, month);
        }

        @Override
        protected void onPostExecute(Document doc){
            if(doc != null) {
                displayTimings(new TimingsParser().extractTimings(doc, year, month, day));
            }
        }
    }

    public void displayTimings(HashMap<String, GregorianCalendar> timings){
        TextView view;
        //SimpleDateFormat format = new SimpleDateFormat("H:mm", Locale.getDefault());
        SimpleDateFormat format = (SimpleDateFormat) SimpleDateFormat.getTimeInstance();
        GregorianCalendar jCal;

        //Fajr
        jCal = timings.get("fajr");
        format.setCalendar(jCal);
        view = (TextView) findViewById(R.id.fajrTime);
        view.setText(format.format(jCal.getTime()));

        //Sunrise
        jCal = timings.get("sunrise");
        format.setCalendar(jCal);
        view = (TextView) findViewById(R.id.sunriseTime);
        view.setText(format.format(jCal.getTime()));

        //Dhuhr
        jCal = timings.get("dhuhr");
        format.setCalendar(jCal);
        view = (TextView) findViewById(R.id.dhuhrTime);
        view.setText(format.format(jCal.getTime()));

        //Asr
        jCal = timings.get("asr");
        format.setCalendar(jCal);
        view = (TextView) findViewById(R.id.asrTime);
        view.setText(format.format(jCal.getTime()));

        //Maghrib
        jCal = timings.get("maghrib");
        format.setCalendar(jCal);
        view = (TextView) findViewById(R.id.maghribTime);
        view.setText(format.format(jCal.getTime()));

        //Isha
        jCal = timings.get("isha");
        format.setCalendar(jCal);
        view = (TextView) findViewById(R.id.ishaTime);
        view.setText(format.format(jCal.getTime()));

        //display date
        view = (TextView) findViewById(R.id.pickedDate);
        view.setText(month + "/" + day);
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
            // Use the current set date as the default date in the picker
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), onDateSetListener, year, month-1, day);
            datePickerDialog.getDatePicker().setMinDate(new GregorianCalendar(year, Calendar.JANUARY, 1).getTimeInMillis());
            datePickerDialog.getDatePicker().setMaxDate(new GregorianCalendar(year, Calendar.DECEMBER, 31).getTimeInMillis());
            return datePickerDialog;
        }

        private void setOnDateSetListener(DatePickerDialog.OnDateSetListener listener) {
            this.onDateSetListener = listener;
        }
    }

    /* Listener for onDateSet */
    public DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            month = monthOfYear+1;
            day = dayOfMonth;
            updateTimings();
        }
    };

    public void pickDate(View v){
        DialogFragment newFragment = DatePickerFragment.newInstance(onDateSetListener);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void setDateToday(View v) {
        Calendar cal = new GregorianCalendar();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        day = cal.get(Calendar.DAY_OF_MONTH);
        updateTimings();
        setOneTime();
    }

    public void setOneTime(){
        AlarmBroadcastReceiver alarmBR;
        Context context = this.getApplicationContext();
        alarmBR = new AlarmBroadcastReceiver();
        alarmBR.setNextAlarm(context);
    }

}
