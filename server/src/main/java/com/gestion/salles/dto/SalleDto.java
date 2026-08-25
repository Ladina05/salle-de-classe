package com.gestion.salles.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SalleDto {

    @NotBlank(message = "Le code salle est obligatoire")
    @Size(max = 20)
    private String codesal;

    @NotBlank(message = "La désignation est obligatoire")
    @Size(max = 120)
    private String designation;

    public SalleDto() {
    }

    public SalleDto(String codesal, String designation) {
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
