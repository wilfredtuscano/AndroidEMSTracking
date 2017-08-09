package com.example.wtuscano.emstracking.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by wtuscano on 7/18/2017.
 */

public class Event implements Serializable {

    private String key;
    private String title;
    private String creator;
    private String coordinatorName;
    private String coordinatorNumber;
    private Location startLocation;
    private Location destinationLocation;
    private Date dateTime;
    private String dateTimeToOrder;
    private ArrayList<Bus> buses = new ArrayList<>();
    private ArrayList<String> infoSheetsURLs = new ArrayList<>();

    public Event() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public ArrayList<Bus> getBuses() {
        return buses;
    }

    public void setBuses(ArrayList<Bus> buses) {
        this.buses = buses;
    }

    public String getCoordinatorName() {
        return coordinatorName;
    }

    public void setCoordinatorName(String coordinatorName) {
        this.coordinatorName = coordinatorName;
    }

    public String getCoordinatorNumber() {
        return coordinatorNumber;
    }

    public void setCoordinatorNumber(String coordinatorNumber) {
        this.coordinatorNumber = coordinatorNumber;
    }

    public ArrayList<String> getInfoSheetsURLs() {
        return infoSheetsURLs;
    }

    public void setInfoSheetsURLs(ArrayList<String> infoSheetsURLs) {
        this.infoSheetsURLs = infoSheetsURLs;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public Location getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(Location destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public String getDateTimeToOrder() {
        return dateTimeToOrder;
    }

    public void setDateTimeToOrder(String dateTimeToOrder) {
        this.dateTimeToOrder = dateTimeToOrder;
    }
}
