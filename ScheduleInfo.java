/**
 * @author Michael Girard
 */
package com.github.michael_girard.scheduler;

import java.time.LocalDate;
import java.time.LocalTime;

public abstract class ScheduleInfo {
    /*
        The start date and start/end times of the objects
        being displayed on the scheduler
    */
    protected LocalDate startDate;
    protected LocalTime startTime;
    protected LocalTime endTime;

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
    
    /*
        Abstract override of toString, so extending classes must
        provide their own toString implementation.
    
        toString is used to determine the label that displays on
        the graphical scheduler.
    */
    @Override
    public abstract String toString();
}
