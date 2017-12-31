/**
 * @author Michael Girard
 */
package com.github.michael_girard.scheduler;

import java.time.LocalDate;
import java.time.LocalTime;


public class SchedulerEntry extends ScheduleInfo{
    /*
        LocalDate startDate, LocalTime startTime, and LocalTime endTime
        are inherited from ScheduleInfo
    */
    String entryTitle;
    String entryDescription;

    
    //<editor-fold defaultstate="collapsed" desc="Constructors">
    public SchedulerEntry(){
        /*
        Entry with default information: Starts right now, lasts 1 hour,
        has a default title and no description
        */
        this(LocalDate.now(), LocalTime.now(), LocalTime.now().plusHours(1), "Appointment", "");
    }
    
    public SchedulerEntry(LocalDate date, LocalTime start, LocalTime end, String title){
        /*
        Entry with the description omitted
        */
        this(date, start, end, title, "");
    }
    
    public SchedulerEntry(LocalDate date, LocalTime start, LocalTime end, String title, String description){
        startDate = date;
        startTime = start;
        endTime = end;
        entryTitle = title;
        entryDescription = description;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public String getEntryTitle() {
        return entryTitle;
    }
    
    public void setEntryTitle(String entryTitle) {
        this.entryTitle = entryTitle;
    }
    
    public String getEntryDescription() {
        return entryDescription;
    }
    
    public void setEntryDescription(String entryDescription) {
        this.entryDescription = entryDescription;
    }
    //</editor-fold>

    @Override
    public String toString() {
        int startHour = startTime.getHour();            //Get the start hour
        String AMPM = (startHour <= 11 ? "AM" : "PM");  //Assign AM or PM
        if (startHour > 11) startHour -= 12;            //Convert to 12 hour time
        
        /*
            Appointments display something like
            "2 PM: Doctor's Appointment"
        */
        return (startHour + " " + AMPM + ": " + getEntryTitle());
    }
    
}
