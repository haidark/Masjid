package com.masjidumar.masjid;

import java.util.GregorianCalendar;

/**
 * Created by Haidar on 6/10/2015.
 * Bundling class for Gregorian calendar and associated label
 */
public class TargetTime {
    private String targetLabel;
    private GregorianCalendar targetCal;

    public TargetTime(String label, GregorianCalendar cal){
        this.targetLabel = label;
        this.targetCal = cal;
    }

    public String getLabel(){ return targetLabel;}
    public void setLabel(String label){ this.targetLabel = label;}
    public GregorianCalendar getCal(){ return targetCal;}
    public void setCal(GregorianCalendar cal){ this.targetCal = cal;}
}
