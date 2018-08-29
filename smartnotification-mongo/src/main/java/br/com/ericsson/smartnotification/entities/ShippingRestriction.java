package br.com.ericsson.smartnotification.entities;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

public class ShippingRestriction extends AgregationDocument {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateStart;

    private List<DayOfWeek> daysOfWeek;

    private LocalTime timeStart;

    private LocalTime timeEnd;

    private int maximumNumberSubmissions;
    
    private int minutesToExpire;

    public ShippingRestriction() {
        super();
    }

    public ShippingRestriction(Date dateStart, List<DayOfWeek> daysOfWeek, LocalTime timeStart, 
    		LocalTime timeEnd, int maximumNumberSubmissions, int minutesToExpire) {
        super();
        this.dateStart = dateStart;
        this.daysOfWeek = daysOfWeek;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.maximumNumberSubmissions = maximumNumberSubmissions;
        this.minutesToExpire = minutesToExpire;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public List<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(List<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public int getMaximumNumberSubmissions() {
        return maximumNumberSubmissions;
    }

    public void setMaximumNumberSubmissions(int maximumNumberSubmissions) {
        this.maximumNumberSubmissions = maximumNumberSubmissions;
    }

    public int getMinutesToExpire() {
        return minutesToExpire;
    }

    public void setMinutesToExpire(int minutesToExpire) {
        this.minutesToExpire = minutesToExpire;
    }

    public LocalTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(LocalTime timeStart) {
        this.timeStart = timeStart;
    }

    public LocalTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(LocalTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
