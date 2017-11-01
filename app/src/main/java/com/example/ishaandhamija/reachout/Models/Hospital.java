<<<<<<< HEAD
package com.example.ishaandhamija.reachout.Models;

/**
 * Created by ishaandhamija on 24/08/17.
 */

public class Hospital {

    String name;
//    String email;
    String phonenumber;
    String address;
    String speciality;
//    String password;
    Double lat;
    Double lon;



    public Hospital(String name, String phonenumber, String address, String speciality, Double lat, Double lon) {
        this.name = name;
        this.phonenumber = phonenumber;
        this.address = address;
        this.speciality = speciality;

        this.lat = lat;
        this.lon = lon;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getName() {
        return name;
    }

    public String getPhonenumber() {
        return phonenumber;
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
=======
package com.example.ishaandhamija.reachout.Models;

/**
 * Created by ishaandhamija on 24/08/17.
 */

public class Hospital {

    String name;
//    String email;
    String phonenumber;
    String address;
//    String password;
    Double lat;
    Double lon;

    public Hospital(String name, String phonenumber, String address, Double lat, Double lon) {
        this.name = name;
        this.phonenumber = phonenumber;
        this.address = address;
        this.lat = lat;
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public String getPhonenumber() {
        return phonenumber;
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
>>>>>>> 69315ac81b1443d6fb43b70075ca2ccec5779aff
