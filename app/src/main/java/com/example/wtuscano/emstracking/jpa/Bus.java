package com.example.wtuscano.emstracking.jpa;

import android.location.*;

import java.io.Serializable;

/**
 * Created by wtuscano on 7/18/2017.
 */

public class Bus implements Serializable {

    private String number;
    private String driverFname;
    private String driverLname;
    private String driverContactNumber;
    private Location currentLocation = new Location();

    public Bus() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDriverFname() {
        return driverFname;
    }

    public void setDriverFname(String driverFname) {
        this.driverFname = driverFname;
    }

    public String getDriverLname() {
        return driverLname;
    }

    public void setDriverLname(String driverLname) {
        this.driverLname = driverLname;
    }

    public String getDriverContactNumber() {
        return driverContactNumber;
    }

    public void setDriverContactNumber(String driverContactNumber) {
        this.driverContactNumber = driverContactNumber;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }
}
