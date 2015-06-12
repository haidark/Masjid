package com.masjidumar.masjid;

import java.util.GregorianCalendar;

/**
 * Created by Haidar on 6/10/2015.
 * Bundling class for Gregorian calendar and associated label
 */
public class TargetTime {
    private String targetLabel;
    private GregorianCalendar targetCal;
    private int stringID;

    public TargetTime(String label, GregorianCalendar cal){
        setLabel(label);
        setCal(cal);
    }

    public TargetTime(String label, GregorianCalendar cal, int id){
        setLabel(label);
        setCal(cal);
        setID(id);
    }

    public String getLabel(){ return targetLabel;}
    public void setLabel(String label){ this.targetLabel = label;}

    public GregorianCalendar getCal(){ return targetCal;}
    public void setCal(GregorianCalendar cal){ this.targetCal = cal;}

    public int getID(){ return stringID;}
    public void setID(int id){ this.stringID = id;}
}
