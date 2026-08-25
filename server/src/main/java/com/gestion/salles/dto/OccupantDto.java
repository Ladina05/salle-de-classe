package com.gestion.salles.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class OccupantDto {

    private Long id;

    @NotBlank(message = "Le code professeur est obligatoire")
    private String codeprof;

    private String nomProf;
    private String prenomProf;

    @NotBlank(message = "Le code salle est obligatoire")
    private String codesal;

    private String designationSalle;

    @NotNull(message = "La date d'occupation est obligatoire")
    private LocalDate date;

    public OccupantDto() {
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
