package com.example.ishaandhamija.reachout.Models;

/**
 * Created by ishaandhamija on 24/08/17.
 */

public class Hospital {

    String name;
    String email;
    Long phonenumber;
    String address;
    String password;
    String driver_name;
    Long drive_pnumber;
    String ambulance;
    String lat;
    String lon;

    public Hospital(String name, String email, Long phonenumber, String address, String password, String driver_name, Long drive_pnumber, String ambulance, String lat, String lon) {
        this.name = name;
        this.email = email;
        this.phonenumber = phonenumber;
        this.address = address;
        this.password = password;
        this.driver_name = driver_name;
        this.drive_pnumber = drive_pnumber;
        this.ambulance = ambulance;
        this.lat = lat;
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Long getPhonenumber() {
        return phonenumber;
    }

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public Long getDrive_pnumber() {
        return drive_pnumber;
    }

    public String getAmbulance() {
        return ambulance;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }
}
