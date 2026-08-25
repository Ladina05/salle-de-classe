package com.gestion.salles.mapper;

import com.gestion.salles.dto.OccupantDto;
import com.gestion.salles.dto.ProfDto;
import com.gestion.salles.dto.SalleDto;
import com.gestion.salles.entity.Occuper;
import com.gestion.salles.entity.Prof;
import com.gestion.salles.entity.Salle;

public final class EntityMapper {

    private EntityMapper() {
    }

    public static ProfDto toDto(Prof entity) {
        return new ProfDto(entity.getCodeprof(), entity.getNom(), entity.getPrenom(), entity.getGrade());
    }

    public static void updateEntity(Prof entity, ProfDto dto) {
        entity.setNom(dto.getNom().trim());
        entity.setPrenom(dto.getPrenom().trim());
        entity.setGrade(dto.getGrade().trim());
    }

    public static Prof toEntity(ProfDto dto) {
        return new Prof(
                dto.getCodeprof().trim(),
                dto.getNom().trim(),
                dto.getPrenom().trim(),
                dto.getGrade().trim()
        );
    }

    public static SalleDto toDto(Salle entity) {
        return new SalleDto(entity.getCodesal(), entity.getDesignation());
    }

    public static void updateEntity(Salle entity, SalleDto dto) {
        entity.setDesignation(dto.getDesignation().trim());
    }

    public static Salle toEntity(SalleDto dto) {
        return new Salle(dto.getCodesal().trim(), dto.getDesignation().trim());
    }

    public static OccupantDto toDto(Occuper entity) {
        OccupantDto dto = new OccupantDto();
        dto.setId(entity.getId());
        dto.setCodeprof(entity.getProf().getCodeprof());
        dto.setNomProf(entity.getProf().getNom());
        dto.setPrenomProf(entity.getProf().getPrenom());
        dto.setCodesal(entity.getSalle().getCodesal());
        dto.setDesignationSalle(entity.getSalle().getDesignation());
        dto.setDate(entity.getDate());
        return dto;
    }
}
