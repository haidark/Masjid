package com.masjidumar.masjid;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileInputStream;
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
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by Haidar on 6/7/2015.
 *
 * Extracts timings from XML Document
 */
public class TimingsParser {

    public Document downloadXMLTimings(String urlStr, File cacheDir, int year, int month){
        try{
            //Form the URL for this month
            URL url = new URL(urlStr+Integer.toString(month)+".xml");

            //Build a DOM
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            // Download the XML file
            Document doc = db.parse(new InputSource(url.openStream()));

            // save the XML file for future use.
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource docSource = new DOMSource(doc);
            StreamResult xmlFile = new StreamResult(new File(cacheDir, "j"+month+".xml"));
            transformer.transform(docSource, xmlFile);

            return doc;
        } catch(Exception e){
            Log.e("ERROR", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Document updateXMLTimings(File cacheDir, int year, int month){
        try {
            FileInputStream xmlFile = new FileInputStream(new File(cacheDir, "j"+month+".xml"));
            //Build a DOM
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            // Open the XML file and parse into Document
            Document doc = db.parse(new InputSource(xmlFile));
            xmlFile.close();

            return doc;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public HashMap<String, GregorianCalendar> extractTimings(Document doc, int year, int month, int day) {

        HashMap<String, GregorianCalendar> timings = new HashMap<String, GregorianCalendar>();

        doc.getDocumentElement().normalize(); //???

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


}
