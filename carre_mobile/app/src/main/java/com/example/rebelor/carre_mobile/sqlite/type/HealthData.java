package com.example.rebelor.carre_mobile.sqlite.type;


import android.util.Log;

import static android.content.ContentValues.TAG;

public class HealthData {

    private long id;
    private String sex;
    private int age;
    private String smoker;
    private String physicalActivity;
    private String chronicKidney;
    private double height, mass, bmi;
    private String hypertension, diabetes;
    private long timestamp;


    public HealthData() {

    }

    public HealthData(String sex, int age, String smoker, String physicalActivity, String chronicKidney,
                      double height, double mass, String hypertension, String diabetes,  double bmi) {
        this.sex = sex;
        this.age = age;
        this.smoker = smoker;
        this.physicalActivity = physicalActivity;
        this.chronicKidney = chronicKidney;
        this.height = height;
        this.mass = mass;
        this.hypertension = hypertension;
        this.diabetes = diabetes;
        this.bmi = bmi;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhysicalActivity() {
        return physicalActivity;
    }

    public String getSmoker() {
        return smoker;
    }

    public void setSmoker(String smoker) {
        this.smoker = smoker;
    }

    public void setPhysicalActivity(String physicalActivity) {
        this.physicalActivity = physicalActivity;
    }

    public String getChronicKidney() {
        return chronicKidney;
    }

    public void setChronicKidney(String chronicKidney) {
        this.chronicKidney = chronicKidney;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public String getHypertension() {
        return hypertension;
    }

    public void setHypertension(String hypertension) {
        this.hypertension = hypertension;
    }

    public String getDiabetes() {
        return diabetes;
    }

    public void setDiabetes(String diabetes) {
        this.diabetes = diabetes;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getBmi() { return bmi;}

    public void setBmi(double bmi) { this.bmi = bmi;}


}
