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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //try to load timings from the previously downloaded file
        Calendar cal = new GregorianCalendar();
        int month = cal.get(Calendar.MONTH) + 1;
        Document doc = null;
        try {
            FileInputStream xmlFile = new FileInputStream(new File(getFilesDir(), "j"+month+".xml"));
            //Build a DOM
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            // Open the XML file
            doc = db.parse(new InputSource(xmlFile));
            xmlFile.close();
        } catch(FileNotFoundException f){
            //if the file is not found, download it.
            new DownloadTimingsXML().execute();
        } catch(Exception e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_main);
        String txt;
        if(doc != null) {
            txt = extractTimings(doc);
        }else{
            txt = "ERROR!";
        }

        TextView tView = (TextView) findViewById(R.id.xmlView);
        tView.setText(txt);
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

    private class DownloadTimingsXML extends AsyncTask<Void, Void, Document> {
        String xmlJURL = getString(R.string.jamaat_URL);
        String xmlPURL = getString(R.string.prayer_URL);
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
        protected Document doInBackground(Void... Params){

            try{
                //get this month
                Calendar cal = new GregorianCalendar();
                int month = cal.get(Calendar.MONTH)+1; //add 1 since it starts at 0

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
                StreamResult outFile = new StreamResult(new File(getFilesDir(), "j"+month+".xml"));
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
            String txt;
            if(doc != null) {
                txt = extractTimings(doc);
            }else{
                txt = "ERROR!";
            }

//            pDialog.dismiss();
            TextView tView = (TextView) findViewById(R.id.xmlView);
            tView.setText(txt);
        }


    }

    public String extractTimings(Document doc) {
        doc.getDocumentElement().normalize(); //???
        // get all the date elements
        NodeList nodes = doc.getElementsByTagName("date");
        //extract timings.
        String txt = "";
        Calendar cal = new GregorianCalendar();
        int j = cal.get(Calendar.DAY_OF_MONTH)-1;

        Element dateElem = (Element) nodes.item(j);
        txt = txt + "Last Updated: " + cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + "\n";

        NodeList fajr = dateElem.getElementsByTagName("fajr");
        String fajrJTime = fajr.item(0).getChildNodes().item(0).getNodeValue();
        txt = txt + getString(R.string.fajr) + ":" + fajrJTime + "\n";

        NodeList sunrise = dateElem.getElementsByTagName("sunrise");
        String sunriseTime = sunrise.item(0).getChildNodes().item(0).getNodeValue();
        txt = txt + getString(R.string.sunrise) + ":" + sunriseTime + "\n";

        NodeList dhuhr = dateElem.getElementsByTagName("dhuhr");
        String dhuhrJTime = dhuhr.item(0).getChildNodes().item(0).getNodeValue();
        txt = txt + getString(R.string.dhuhr) + ":" + dhuhrJTime  + "\n";

        NodeList asr = dateElem.getElementsByTagName("asr");
        String asrJTime = asr.item(0).getChildNodes().item(0).getNodeValue();
        txt = txt + getString(R.string.asr) + ":" + asrJTime + "\n";

        NodeList maghrib = dateElem.getElementsByTagName("maghrib");
        String maghribJTime = maghrib.item(0).getChildNodes().item(0).getNodeValue();
        txt = txt + getString(R.string.maghrib) + ":" + maghribJTime + "\n";

        NodeList isha = dateElem.getElementsByTagName("isha");
        String ishaJTime = isha.item(0).getChildNodes().item(0).getNodeValue();
        txt = txt + getString(R.string.isha) + ":" + ishaJTime + "\n";
        return txt;
    }
}
