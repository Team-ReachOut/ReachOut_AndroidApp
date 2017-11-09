package com.example.ishaandhamija.reachout.Models;

import org.json.JSONArray;

/**
 * Created by ishaandhamija on 24/08/17.
 */

public class Hospital {

    String name;
//    String email;
    String phone1;
    String phone2;
    String phone3;
    String address;
    JSONArray specialities;
    JSONArray services;
//    String password;
    Double lat;
    Double lon;

    public Hospital(String name, String phone1, String phone2, String phone3, String address, JSONArray specialities, JSONArray services, Double lat, Double lon) {
        this.name = name;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.phone3 = phone3;
        this.address = address;
        this.specialities = specialities;
        this.services = services;
        this.lat = lat;
        this.lon = lon;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getPhone3() {
        return phone3;
    }

    public void setPhone3(String phone3) {
        this.phone3 = phone3;
    }

    public JSONArray getSpecialities() {
        return specialities;
    }

    public void setSpecialities(JSONArray specialities) {
        this.specialities = specialities;
    }

    public JSONArray getServices() {
        return services;
    }

    public void setServices(JSONArray services) {
        this.services = services;
    }

    public String getName() {
        return name;
    }



    public String getAddress() {
        return address;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }
}

