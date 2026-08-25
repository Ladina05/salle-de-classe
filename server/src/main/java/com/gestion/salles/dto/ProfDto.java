package com.gestion.salles.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProfDto {

    @NotBlank(message = "Le code professeur est obligatoire")
    @Size(max = 20)
    private String codeprof;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 80)
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(max = 80)
    private String prenom;

    @NotBlank(message = "Le grade est obligatoire")
    @Size(max = 60)
    private String grade;

    public ProfDto() {
    }

    public ProfDto(String codeprof, String nom, String prenom, String grade) {
        this.codeprof = codeprof;
        this.nom = nom;
        this.prenom = prenom;
        this.grade = grade;
    }

    public String getCodeprof() {
        return codeprof;
    }

    public void setCodeprof(String codeprof) {
        this.codeprof = codeprof;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
