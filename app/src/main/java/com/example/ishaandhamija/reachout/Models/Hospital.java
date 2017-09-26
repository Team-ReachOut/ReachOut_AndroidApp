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
    String lat;
    String lon;

    public Hospital(String name, String email, Long phonenumber, String address, String password, String lat, String lon) {
        this.name = name;
        this.email = email;
        this.phonenumber = phonenumber;
        this.address = address;
        this.password = password;
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

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }
}
