package com.example.rebelor.carre_mobile.sqlite.type;



public class Observable {
    private long id;
    private String observable;
    private String name;



    private String modifiable;


    private String measurement;

    public Observable(){}

    public Observable(long id, String name, String observable, String measurement){
        this.id = id;
        this.observable = observable;
        this.name = name;
        this.measurement = measurement;
        this.modifiable = modifiable;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getObservable() {
        return observable;
    }

    public void setObservable(String observable) {
        this.observable = observable;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public String getModifiable() {
        return modifiable;
    }

    public void setModifiable(String modifiable) {
        this.modifiable = modifiable;
    }
}
