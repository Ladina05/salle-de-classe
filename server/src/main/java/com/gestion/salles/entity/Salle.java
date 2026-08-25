package com.gestion.salles.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "salle")
public class Salle {

    @Id
    @Column(name = "codesal", length = 20, nullable = false)
    @NotBlank(message = "Le code salle est obligatoire")
    @Size(max = 20)
    private String codesal;

    @Column(name = "designation", length = 120, nullable = false)
    @NotBlank(message = "La désignation est obligatoire")
    @Size(max = 120)
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
}
