package com.gestion.salles.desktop.model;

import java.time.LocalDate;

public class Occupant {

    private Long id;
    private String codeprof;
    private String nomProf;
    private String prenomProf;
    private String codesal;
    private String designationSalle;
    private LocalDate date;

    public Occupant() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeprof() {
        return codeprof;
    }

    public void setCodeprof(String codeprof) {
        this.codeprof = codeprof;
    }

    public String getNomProf() {
        return nomProf;
    }

    public void setNomProf(String nomProf) {
        this.nomProf = nomProf;
    }

    public String getPrenomProf() {
        return prenomProf;
    }

    public void setPrenomProf(String prenomProf) {
        this.prenomProf = prenomProf;
    }

    public String getCodesal() {
        return codesal;
    }

    public void setCodesal(String codesal) {
        this.codesal = codesal;
    }

    public String getDesignationSalle() {
        return designationSalle;
    }

    public void setDesignationSalle(String designationSalle) {
        this.designationSalle = designationSalle;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
