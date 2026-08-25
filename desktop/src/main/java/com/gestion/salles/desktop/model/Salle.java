package com.gestion.salles.desktop.model;

public class Salle {

    private String codesal;
    private String designation;

    public Salle() {
    }

    public Salle(String codesal, String designation) {
        this.codesal = codesal;
        this.designation = designation;
    }

    public String getCodesal() {
        return codesal;
    }

    public void setCodesal(String codesal) {
        this.codesal = codesal;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    @Override
    public String toString() {
        return codesal + " — " + designation;
    }
}
