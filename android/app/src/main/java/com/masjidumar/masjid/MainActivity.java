package com.masjidumar.masjid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class MainActivity extends ActionBarActivity {
    public final static String EXTRA_MESSAGE="com.masjidumar.masjid.MESSAGE";
    ProgressDialog pDialog;
    String xmlURL = "http://www.masjidumar.com/files/timings/j";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        Calendar cal = new GregorianCalendar();
        int month = cal.get(Calendar.MONTH)+1; //add 1 since it starts at 0
        new DownloadTimingsXML().execute(xmlURL + Integer.toString(month)+".xml");
    }

    private class DownloadTimingsXML extends AsyncTask<String, Void, NodeList> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
/*            //create a progressbar
            pDialog = new ProgressDialog(MainActivity.this);
            // set the title
            pDialog.setTitle("Updating jamaat times");
            // Set the progressbar message
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false); //what does this do???
            //Show the progressbar
            pDialog.show();*/
        }

        @Override
        protected NodeList doInBackground(String... xmlUrl){
            NodeList nodelist = null;
            try{
                URL url = new URL(xmlUrl[0]);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                // Download the XML file
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize(); //???
                //locate the date tags
                nodelist = doc.getElementsByTagName("date");
            } catch(Exception e){
                Log.e("ERROR", e.getMessage());
                e.printStackTrace();
            }
            return nodelist;
        }

        @Override
        protected void onPostExecute(NodeList nodes){
            //Extract timings.
            String txt = "";
            Calendar cal = new GregorianCalendar();
            int j = cal.get(Calendar.DAY_OF_MONTH)-1;

            Element dateElem = (Element) nodes.item(j);
            txt = txt + "Last Updated: " + cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + "\n";

            NodeList fajr = dateElem.getElementsByTagName("fajr");

            String fajrJTime = fajr.item(0).getChildNodes().item(0).getNodeValue();
            txt = txt + "Fajr: " + fajrJTime + "\n";

            NodeList sunrise = dateElem.getElementsByTagName("sunrise");
            String sunriseTime = sunrise.item(0).getChildNodes().item(0).getNodeValue();
            txt = txt + "Sunrise: " + sunriseTime + "\n";

            NodeList dhuhr = dateElem.getElementsByTagName("dhuhr");
            String dhuhrJTime = dhuhr.item(0).getChildNodes().item(0).getNodeValue();
            txt = txt + "Dhuhr: " + dhuhrJTime  + "\n";

            NodeList asr = dateElem.getElementsByTagName("asr");
            String asrJTime = asr.item(0).getChildNodes().item(0).getNodeValue();
            txt = txt + "Asr: " + asrJTime + "\n";

            NodeList maghrib = dateElem.getElementsByTagName("maghrib");
            String maghribJTime = maghrib.item(0).getChildNodes().item(0).getNodeValue();
            txt = txt + "Maghrib: " + maghribJTime + "\n";

            NodeList isha = dateElem.getElementsByTagName("isha");
            String ishaJTime = isha.item(0).getChildNodes().item(0).getNodeValue();
            txt = txt + "Isha: " + ishaJTime + "\n";

//            pDialog.dismiss();
            TextView tView = (TextView) findViewById(R.id.xmlView);
            tView.setText(txt);
        }
    }
}
