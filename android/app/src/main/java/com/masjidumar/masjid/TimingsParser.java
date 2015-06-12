package com.masjidumar.masjid;

import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.util.Xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by Haidar on 6/7/2015.
 *
 * Extracts timings from XML Document
 */
public class TimingsParser {

    public HashMap<String, GregorianCalendar> downloadXMLTimings(String urlStr, File cacheDir, GregorianCalendar pickedDate)
        throws IOException{
        //Form the URL
        try {
            URL url = new URL(urlStr);
            InputStream urlStream = url.openStream();
            synchronized (this) {
                // save the file locally
                OutputStream oStream = new FileOutputStream(new File(cacheDir, "jamaat_timings.xml"));

                byte[] b = new byte[2048];
                int length;

                while ((length = urlStream.read(b)) != -1) {
                    oStream.write(b, 0, length);
                }

                oStream.close();
                urlStream.close();
            }
        } catch (IOException e){
            Log.w("DownloadXML:", e.getMessage());
            Log.i("DownloadXML:", "Failed to download the XML timings, handle gracefully and return cached copy");
        }
        return updateXMLTimings(cacheDir, pickedDate);
    }

    public HashMap<String, GregorianCalendar> updateXMLTimings(File cacheDir, GregorianCalendar pickedDate)
            throws IOException{
        HashMap<String, GregorianCalendar> timings;
        synchronized (this) {
            FileInputStream xmlStream = new FileInputStream(new File(cacheDir, "jamaat_timings.xml"));
            timings = parseTimings(xmlStream, pickedDate);
            xmlStream.close();
        }
        return timings;
    }

    public HashMap<String, GregorianCalendar> parseTimings(InputStream inStream, GregorianCalendar pickedDate)
            throws IOException{

        //get the year, month, and day from the pickedDate
        int year = pickedDate.get(GregorianCalendar.YEAR);
        int month = pickedDate.get(GregorianCalendar.MONTH)+1;
        int day = pickedDate.get(GregorianCalendar.DAY_OF_MONTH);

        //create the hashmap and populate it with null values
        HashMap<String, GregorianCalendar> timings = new HashMap<String, GregorianCalendar>();
        String[] pNames = {"fajr", "sunrise", "dhuhr", "asr", "maghrib", "isha"};
        for( String pName : pNames) {
            timings.put(pName, null);
        }

        // instantiate the parser
        XmlPullParser parser = Xml.newPullParser();
        
        //initialize formatter
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String jTime;
        GregorianCalendar jCal;
        String pName;

        // parse the XML file
        try{
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inStream, null);

            parser = getTag(parser, "year", "value", Integer.toString(year));
            parser = getTag(parser, "month", "value", Integer.toString(month));
            parser = getTag(parser, "date", "day", Integer.toString(day));


            //Fajr
            pName="fajr";
            parser = getTag(parser, pName);
            jTime = parser.nextText();
            jCal = new GregorianCalendar();
            jCal.setTime(format.parse(jTime));
            jCal.set(year, month - 1, day);
            timings.put(pName, jCal);
            Log.v("ParseXML:", "Got Fajr Time");

            //Sunrise
            pName="sunrise";
            parser = getTag(parser, pName);
            jTime = parser.nextText();
            jCal = new GregorianCalendar();
            jCal.setTime(format.parse(jTime));
            jCal.set(year, month - 1, day);
            timings.put(pName, jCal);
            Log.v("ParseXML:", "Got Sunrise Time");

            //Dhuhr
            pName="dhuhr";
            parser = getTag(parser, pName);
            jTime = parser.nextText();
            jCal = new GregorianCalendar();
            jCal.setTime(format.parse(jTime));
            jCal.set(year, month-1, day);
            //Dhuhr AM-PM is tricky since its on the cusp, here is some logic to work around
            //permanent solution is to embed this information in the XML files
            int dhuhrHour = jCal.get(GregorianCalendar.HOUR);
            if(!(dhuhrHour >= 9 && dhuhrHour <= 11)){  // if dhuhr is not between 9 and 11 it is AM
                // add 12 hours
                jCal.add(GregorianCalendar.HOUR, 12);
            }
            timings.put(pName, jCal);
            Log.v("ParseXML:", "Got Dhuhr Time");

            //Asr
            pName="asr";
            parser = getTag(parser, pName);
            jTime = parser.nextText();
            jCal = new GregorianCalendar();
            jCal.setTime(format.parse(jTime));
            jCal.set(year, month - 1, day);
            jCal.add(GregorianCalendar.HOUR, 12);   //change to PM
            timings.put(pName, jCal);
            Log.v("ParseXML:", "Got Asr Time");

            //Maghrib
            pName="maghrib";
            parser = getTag(parser, pName);
            jTime = parser.nextText();
            jCal = new GregorianCalendar();
            jCal.setTime(format.parse(jTime));
            jCal.set(year, month - 1, day);
            jCal.add(GregorianCalendar.HOUR, 12);   //change to PM
            timings.put(pName, jCal);
            Log.v("ParseXML:", "Got Maghrib Time");

            //Isha
            pName="isha";
            parser = getTag(parser, pName);
            jTime = parser.nextText();
            jCal = new GregorianCalendar();
            jCal.setTime(format.parse(jTime));
            jCal.set(year, month - 1, day);
            jCal.add(GregorianCalendar.HOUR, 12);   //change to PM
            timings.put(pName, jCal);
            Log.v("ParseXML:", "Got Isha Time");

        } catch (ParseException | XmlPullParserException | NoSuchFieldException e) {
            Log.w("ParseXML:", e.getMessage());
        }
        return timings;
    }

    public XmlPullParser getTag(XmlPullParser parser, String tag)
            throws XmlPullParserException, IOException, NoSuchFieldException{
        if( parser != null) {
            int event = parser.getEventType();
            // while the document has not ended
            while (event != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();

                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (tagName.equals(tag)) {
                            return parser;
                        }
                 }
                event = parser.next();
            }
        } else {
            throw new NoSuchFieldException("Could not get tag: <" + tag + ">");
        }
        return null;
    }

    public XmlPullParser getTag(XmlPullParser parser, String tag, String attr, String value)
            throws XmlPullParserException, IOException, NoSuchFieldException{
        if( parser != null) {
            int event = parser.getEventType();
            // while the document has not ended
            while (event != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();

                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (tagName.equals(tag) && parser.getAttributeValue(null, attr).equals(value)) {
                            return parser;
                        }
                }
                event = parser.next();
            }
        }
        else{
            throw new NoSuchFieldException("Could not get tag: <" + tag + "> with attribute: "+attr);
        }
        return null;
    }
}
