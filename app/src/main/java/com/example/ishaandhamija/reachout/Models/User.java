package com.example.ishaandhamija.reachout.Models;

/**
 * Created by HP on 24-Aug-17.
 */

public class User {

    String name;
    String age;
    String bloodgroup;
    String address;
    String contactno;
    String email;
    String password;
    String sex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public User(String name, String age, String bloodgroup, String address, String contactno, String email, String password, String sex) {

        this.name = name;
        this.age = age;
        this.bloodgroup = bloodgroup;
        this.address = address;
        this.contactno = contactno;
        this.email = email;
        this.password = password;
        this.sex = sex;
    }
}
