package com.gestion.salles.service;

import com.gestion.salles.dto.ProfDto;
import com.gestion.salles.entity.Prof;
import com.gestion.salles.exception.BusinessException;
import com.gestion.salles.exception.ResourceNotFoundException;
import com.gestion.salles.mapper.EntityMapper;
import com.gestion.salles.repository.OccuperRepository;
import com.gestion.salles.repository.ProfRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProfService {

    private final ProfRepository profRepository;
    private final OccuperRepository occuperRepository;

    public ProfService(ProfRepository profRepository, OccuperRepository occuperRepository) {
        this.profRepository = profRepository;
        this.occuperRepository = occuperRepository;
    }

    @Transactional(readOnly = true)
    public List<ProfDto> lister() {
        return profRepository.findAll().stream().map(EntityMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public ProfDto trouver(String codeprof) {
        return EntityMapper.toDto(getOrThrow(codeprof));
    }

    @Transactional(readOnly = true)
    public List<ProfDto> rechercher(String terme) {
        if (terme == null || terme.isBlank()) {
            return lister();
        }
        return profRepository.rechercherParCodeOuNom(terme.trim()).stream()
                .map(EntityMapper::toDto)
                .toList();
    }

    public ProfDto creer(ProfDto dto) {
        String code = dto.getCodeprof().trim();
        if (profRepository.existsById(code)) {
            throw new BusinessException("Un professeur avec le code " + code + " existe déjà.");
        }
        return EntityMapper.toDto(profRepository.save(EntityMapper.toEntity(dto)));
    }

    public ProfDto modifier(String codeprof, ProfDto dto) {
        Prof existing = getOrThrow(codeprof);
        EntityMapper.updateEntity(existing, dto);
        return EntityMapper.toDto(profRepository.save(existing));
    }

    public void supprimer(String codeprof) {
        getOrThrow(codeprof);
        if (occuperRepository.existsByProf_Codeprof(codeprof)) {
            throw new BusinessException("Impossible de supprimer ce professeur : il a des occupations enregistrées.");
        }
        profRepository.deleteById(codeprof);
    }

    public Prof getOrThrow(String codeprof) {
        return profRepository.findById(codeprof)
                .orElseThrow(() -> new ResourceNotFoundException("Professeur introuvable : " + codeprof));
    }
}
