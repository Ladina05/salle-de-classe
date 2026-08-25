package com.gestion.salles.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "prof")
public class Prof {

    @Id
    @Column(name = "codeprof", length = 20, nullable = false)
    @NotBlank(message = "Le code professeur est obligatoire")
    @Size(max = 20)
    private String codeprof;

    @Column(name = "nom", length = 80, nullable = false)
    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 80)
    private String nom;

    @Column(name = "prenom", length = 80, nullable = false)
    @NotBlank(message = "Le prénom est obligatoire")
    @Size(max = 80)
    private String prenom;

    @Column(name = "grade", length = 60, nullable = false)
    @NotBlank(message = "Le grade est obligatoire")
    @Size(max = 60)
    private String grade;

    public Prof() {
    }

    public Prof(String codeprof, String nom, String prenom, String grade) {
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
