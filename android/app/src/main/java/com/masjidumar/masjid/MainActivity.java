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
import java.util.Calendar;
import java.util.GregorianCalendar;

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
    static int once = 0;

    private AlarmBroadcastReceiver alarmBR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //try to load timings from the previously downloaded file
        Calendar cal = new GregorianCalendar();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
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
        new DownloadTimingsXML().execute();
    }

    public void updateTimings(){
        new UpdateTimingsXML().execute();
        setOneTime();
    }

    private class UpdateTimingsXML extends AsyncTask<Void, Void, Document> {

        @Override
        protected Document doInBackground(Void... Params){
            try {
                FileInputStream xmlFile = new FileInputStream(new File(getCacheDir(), "j"+month+".xml"));
                //Build a DOM
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();

                // Open the XML file
                Document doc = db.parse(new InputSource(xmlFile));
                xmlFile.close();
                return doc;
            } catch(Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Document doc){
            if(doc != null) {
                extractTimings(doc);
            } else{
                new DownloadTimingsXML().execute();
            }
        }

    }

    private class DownloadTimingsXML extends AsyncTask<Void, Void, Document> {
        String xmlJURL = getString(R.string.jamaat_URL);
        String xmlPURL = getString(R.string.prayer_URL);

        @Override
        protected Document doInBackground(Void... Params){
            try{
                //Form the URL for this month
                URL url = new URL(xmlJURL+Integer.toString(month)+".xml");

                //Build a DOM
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();

                // Download the XML file
                Document doc = db.parse(new InputSource(url.openStream()));

                // save the XML file for future use.
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                DOMSource docSource = new DOMSource(doc);
                StreamResult outFile = new StreamResult(new File(getCacheDir(), "j"+month+".xml"));
                transformer.transform(docSource, outFile);

                Log.d("XML:","Done saving file!");
                return doc;
            } catch(Exception e){
                Log.e("ERROR", e.getMessage());
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Document doc){
            if(doc != null) {
                extractTimings(doc);
            }
        }
    }

    public void extractTimings(Document doc) {
        doc.getDocumentElement().normalize(); //???
        // get all the date elements
        NodeList nodes = doc.getElementsByTagName("date");
        //extract timings.
        String txt;
        TextView view;

        int j = day-1;
        Element dateElem = (Element) nodes.item(j);

        //Fajr
        NodeList fajr = dateElem.getElementsByTagName("fajr");
        String fajrJTime = fajr.item(0).getChildNodes().item(0).getNodeValue();
        txt = fajrJTime + "\n";
        view = (TextView) findViewById(R.id.fajrTime);
        view.setText(txt);

        //sunrise
        NodeList sunrise = dateElem.getElementsByTagName("sunrise");
        String sunriseTime = sunrise.item(0).getChildNodes().item(0).getNodeValue();
        txt = sunriseTime + "\n";
        view = (TextView) findViewById(R.id.sunriseTime);
        view.setText(txt);

        //dhuhr
        NodeList dhuhr = dateElem.getElementsByTagName("dhuhr");
        String dhuhrJTime = dhuhr.item(0).getChildNodes().item(0).getNodeValue();
        txt = dhuhrJTime  + "\n";
        view = (TextView) findViewById(R.id.dhuhrTime);
        view.setText(txt);

        //asr
        NodeList asr = dateElem.getElementsByTagName("asr");
        String asrJTime = asr.item(0).getChildNodes().item(0).getNodeValue();
        txt = asrJTime + "\n";
        view = (TextView) findViewById(R.id.asrTime);
        view.setText(txt);

        //maghrib
        NodeList maghrib = dateElem.getElementsByTagName("maghrib");
        String maghribJTime = maghrib.item(0).getChildNodes().item(0).getNodeValue();
        txt = maghribJTime + "\n";
        view = (TextView) findViewById(R.id.maghribTime);
        view.setText(txt);

        //isha
        NodeList isha = dateElem.getElementsByTagName("isha");
        String ishaJTime = isha.item(0).getChildNodes().item(0).getNodeValue();
        txt = ishaJTime + "\n";
        view = (TextView) findViewById(R.id.ishaTime);
        view.setText(txt);

        TextView datepicked = (TextView) findViewById(R.id.pickedDate);
        datepicked.setText(month + "/" + day);
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
    }

    public void setOneTime(){
        Context context = this.getApplicationContext();
        alarmBR = new AlarmBroadcastReceiver();
        alarmBR.setNextAlarm(context);
    }

}
