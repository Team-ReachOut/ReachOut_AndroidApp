package com.example.ishaandhamija.reachout.Models;

/**
 * Created by HP on 20-Sep-17.
 */

public class Relative {
    String name;
    String age;
    String bGroup;

    public Relative(String name, String age, String bGroup) {

        this.name = name;
        this.age = age;
        this.bGroup = bGroup;
    }

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

    public String getbGroup() {
        return bGroup;
    }

    public void setbGroup(String bGroup) {
        this.bGroup = bGroup;
    }


}
