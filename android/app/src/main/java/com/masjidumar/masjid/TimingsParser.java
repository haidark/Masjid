package com.masjidumar.masjid;

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

    public HashMap<String, GregorianCalendar> downloadXMLTimings(String urlStr, File cacheDir, int year, int month, int day)
            throws IOException{
        //Form the URL
        URL url = new URL(urlStr);
        InputStream urlStream = url.openStream();

        // save the file locally
        OutputStream oStream = new FileOutputStream(new File(cacheDir,  "jamaat_timings.xml"));

        byte[] b = new byte[2048];
        int length;

        while((length = urlStream.read(b)) != -1){
            oStream.write(b,0, length);
        }

        oStream.close();
        urlStream.close();

        return updateXMLTimings(cacheDir, year, month, day);
    }

    public HashMap<String, GregorianCalendar> updateXMLTimings(File cacheDir, int year, int month, int day)
            throws IOException{

        FileInputStream xmlStream = new FileInputStream(new File(cacheDir, "jamaat_timings.xml"));
        HashMap<String, GregorianCalendar> timings = parseTimings(xmlStream, year, month, day);
        xmlStream.close();

        return timings;
    }

    public HashMap<String, GregorianCalendar> parseTimings(InputStream inStream, int year, int month, int day)
            throws IOException{
        //create the hashmap and populate it with dummy values
        HashMap<String, GregorianCalendar> timings = new HashMap<String, GregorianCalendar>();
        timings.put("fajr", null);
        timings.put("sunrise", null);
        timings.put("dhuhr", null);
        timings.put("asr", null);
        timings.put("maghrib", null);
        timings.put("isha", null);

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
            jCal.set(year, month, day);
            timings.put(pName, jCal);

            //Sunrise
            pName="sunrise";
            parser = getTag(parser, pName);
            jTime = parser.nextText();
            jCal = new GregorianCalendar();
            jCal.setTime(format.parse(jTime));
            jCal.set(year, month, day);
            timings.put(pName, jCal);

            //Dhuhr
            pName="dhuhr";
            parser = getTag(parser, pName);
            jTime = parser.nextText();
            jCal = new GregorianCalendar();
            jCal.setTime(format.parse(jTime));
            jCal.set(year, month, day);
            //Dhuhr AM-PM is tricky since its on the cusp, here is some logic to work around
            //permanent solution is to embed this information in the XML files
            int dhuhrHour = jCal.get(GregorianCalendar.HOUR);
            if(!(dhuhrHour >= 9 && dhuhrHour <= 11)){  // if dhuhr is not between 9 and 11 it is AM
                // add 12 hours
                jCal.add(GregorianCalendar.HOUR, 12);
            }
            timings.put(pName, jCal);

            //Asr
            pName="asr";
            parser = getTag(parser, pName);
            jTime = parser.nextText();
            jCal = new GregorianCalendar();
            jCal.setTime(format.parse(jTime));
            jCal.set(year, month, day);
            jCal.add(GregorianCalendar.HOUR, 12);   //change to PM
            timings.put(pName, jCal);

            //Maghrib
            pName="maghrib";
            parser = getTag(parser, pName);
            jTime = parser.nextText();
            jCal = new GregorianCalendar();
            jCal.setTime(format.parse(jTime));
            jCal.set(year, month, day);
            jCal.add(GregorianCalendar.HOUR, 12);   //change to PM
            timings.put(pName, jCal);

            //Isha
            pName="isha";
            parser = getTag(parser, pName);
            jTime = parser.nextText();
            jCal = new GregorianCalendar();
            jCal.setTime(format.parse(jTime));
            jCal.set(year, month, day);
            jCal.add(GregorianCalendar.HOUR, 12);   //change to PM
            timings.put(pName, jCal);

        } catch (ParseException | XmlPullParserException e) {
            e.printStackTrace();
        }
        return timings;
    }

    public HashMap<String, GregorianCalendar> extractTimings(Document doc, int year, int month, int day) {

        HashMap<String, GregorianCalendar> timings = new HashMap<String, GregorianCalendar>();

        doc.getDocumentElement().normalize(); //???

        /*TODO add code for finding the proper year, month, and day*/

        NodeList yearNodes = doc.getElementsByTagName("year");


        // get all the date elements
        NodeList nodes = doc.getElementsByTagName("date");

        //extract timings for this day
        Element dateElem = (Element) nodes.item(day-1);

        //initialize formatter
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        NodeList node;
        String jTime;
        GregorianCalendar jCal;
        String pName;

        try {
            //Fajr
            pName="fajr";
            node = dateElem.getElementsByTagName(pName);
            jTime = node.item(0).getChildNodes().item(0).getNodeValue();
            jCal = new GregorianCalendar();
            jCal.setTime(format.parse(jTime));
            jCal.set(year, month, day);
            timings.put(pName, jCal);

            //Sunrise
            pName="sunrise";
            node = dateElem.getElementsByTagName(pName);
            jTime = node.item(0).getChildNodes().item(0).getNodeValue();
            jCal = new GregorianCalendar();
            jCal.setTime(format.parse(jTime));
            jCal.set(year, month, day);
            timings.put(pName, jCal);

            //Dhuhr
            pName="dhuhr";
            node = dateElem.getElementsByTagName(pName);
            jTime = node.item(0).getChildNodes().item(0).getNodeValue();
            jCal = new GregorianCalendar();
            jCal.setTime(format.parse(jTime));
            jCal.set(year, month, day);
            //Dhuhr AM-PM is tricky since its on the cusp, here is some logic to work around
            //permanent solution is to embed this information in the XML files
            int dhuhrHour = jCal.get(GregorianCalendar.HOUR);
            System.out.println(dhuhrHour);
            if(dhuhrHour >= 9 && dhuhrHour <= 11){  // if dhuhr is between 9 and 11 it is AM
                // dont add 12 hours
            } else {                                // otherwise it should be PM
                jCal.add(GregorianCalendar.HOUR, 12);
            }
            timings.put(pName, jCal);

            //Asr
            pName="asr";
            node = dateElem.getElementsByTagName(pName);
            jTime = node.item(0).getChildNodes().item(0).getNodeValue();
            jCal = new GregorianCalendar();
            jCal.setTime(format.parse(jTime));
            jCal.set(year, month, day);
            jCal.add(GregorianCalendar.HOUR, 12);   //change to PM
            timings.put(pName, jCal);

            //Maghrib
            pName="maghrib";
            node = dateElem.getElementsByTagName(pName);
            jTime = node.item(0).getChildNodes().item(0).getNodeValue();
            jCal = new GregorianCalendar();
            jCal.setTime(format.parse(jTime));
            jCal.set(year, month, day);
            jCal.add(GregorianCalendar.HOUR, 12);   //change to PM
            timings.put(pName, jCal);

            //Isha
            pName="isha";
            node = dateElem.getElementsByTagName(pName);
            jTime = node.item(0).getChildNodes().item(0).getNodeValue();
            jCal = new GregorianCalendar();
            jCal.setTime(format.parse(jTime));
            jCal.set(year, month, day);
            jCal.add(GregorianCalendar.HOUR, 12);   //change to PM
            timings.put(pName, jCal);


        } catch (ParseException e) {
            e.printStackTrace();
            //TODO: Put null in timings?
        }
        return timings;
    }

    public XmlPullParser getTag(XmlPullParser parser, String tag)
            throws XmlPullParserException, IOException{
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
        }
        return null;
    }

    public XmlPullParser getTag(XmlPullParser parser, String tag, String attr, String value)
            throws XmlPullParserException, IOException{
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
        return null;
    }
}
