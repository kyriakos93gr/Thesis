package com.example.rebelor.carre_mobile.sqlite.type;

public class RiskElement {
    private int id;
    private String risk_element;
    private String name;
    private String author;
    private String observable;
    private String modifiable;

    public RiskElement(){}

    public RiskElement(int id, String risk_element, String name, String author, String observable, String modifiable){
        this.id = id;
        this.risk_element = risk_element;
        this.name = name;
        this.author = author;
        this.observable = observable;
        this.modifiable = modifiable;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getRiskelement() {
        return risk_element;
    }

    public void setRiskelement(String risk_element) {
        this.risk_element = risk_element;
    }
    
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getAuthor() { return author; }

    public void setAuthor(String author) { this.author = author; }

    public String getObservable() { return observable; }

    public void setObservable(String observable) { this.observable = observable; }

    public String getModifiable() { return modifiable; }

    public void setModifiable(String modifiable) { this.modifiable = modifiable; }
}
