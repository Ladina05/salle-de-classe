package com.gestion.salles.service;

import com.gestion.salles.dto.OccupantDto;
import com.gestion.salles.entity.Occuper;
import com.gestion.salles.entity.Prof;
import com.gestion.salles.entity.Salle;
import com.gestion.salles.exception.BusinessException;
import com.gestion.salles.exception.ResourceNotFoundException;
import com.gestion.salles.mapper.EntityMapper;
import com.gestion.salles.repository.OccuperRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OccupantService {

    private final OccuperRepository occuperRepository;
    private final ProfService profService;
    private final SalleService salleService;

    public OccupantService(OccuperRepository occuperRepository, ProfService profService, SalleService salleService) {
        this.occuperRepository = occuperRepository;
        this.profService = profService;
        this.salleService = salleService;
    }

    @Transactional(readOnly = true)
    public List<OccupantDto> lister() {
        return occuperRepository.findAllWithRelations().stream().map(EntityMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public OccupantDto trouver(Long id) {
        return EntityMapper.toDto(getOrThrow(id));
    }

    public OccupantDto creer(OccupantDto dto) {
        Prof prof = profService.getOrThrow(dto.getCodeprof().trim());
        Salle salle = salleService.getOrThrow(dto.getCodesal().trim());
        verifierDisponibilite(salle.getCodesal(), dto.getDate(), null);

        if (occuperRepository.existsByProf_CodeprofAndSalle_CodesalAndDate(
                prof.getCodeprof(), salle.getCodesal(), dto.getDate())) {
            throw new BusinessException("Cette occupation existe déjà.");
        }

        Occuper occuper = new Occuper();
        occuper.setProf(prof);
        occuper.setSalle(salle);
        occuper.setDate(dto.getDate());
        return EntityMapper.toDto(occuperRepository.save(occuper));
    }

    public OccupantDto modifier(Long id, OccupantDto dto) {
        Occuper occuper = getOrThrow(id);
        Prof prof = profService.getOrThrow(dto.getCodeprof().trim());
        Salle salle = salleService.getOrThrow(dto.getCodesal().trim());
        verifierDisponibilite(salle.getCodesal(), dto.getDate(), id);

        occuper.setProf(prof);
        occuper.setSalle(salle);
        occuper.setDate(dto.getDate());
        return EntityMapper.toDto(occuperRepository.save(occuper));
    }

    public void supprimer(Long id) {
        getOrThrow(id);
        occuperRepository.deleteById(id);
    }

    private void verifierDisponibilite(String codesal, java.time.LocalDate date, Long idCourant) {
        boolean conflit = idCourant == null
                ? occuperRepository.existsBySalle_CodesalAndDate(codesal, date)
                : occuperRepository.existsBySalle_CodesalAndDateAndIdNot(codesal, date, idCourant);
        if (conflit) {
            throw new BusinessException("La salle " + codesal + " est déjà occupée à la date " + date + ".");
        }
    }

    private Occuper getOrThrow(Long id) {
        return occuperRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Occupation introuvable : " + id));
    }
}
