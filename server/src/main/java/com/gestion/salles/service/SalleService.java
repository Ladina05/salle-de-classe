package com.gestion.salles.service;

import com.gestion.salles.dto.SalleDto;
import com.gestion.salles.entity.Salle;
import com.gestion.salles.exception.BusinessException;
import com.gestion.salles.exception.ResourceNotFoundException;
import com.gestion.salles.mapper.EntityMapper;
import com.gestion.salles.repository.OccuperRepository;
import com.gestion.salles.repository.SalleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SalleService {

    private final SalleRepository salleRepository;
    private final OccuperRepository occuperRepository;

    public SalleService(SalleRepository salleRepository, OccuperRepository occuperRepository) {
        this.salleRepository = salleRepository;
        this.occuperRepository = occuperRepository;
    }

    @Transactional(readOnly = true)
    public List<SalleDto> lister() {
        return salleRepository.findAll().stream().map(EntityMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public SalleDto trouver(String codesal) {
        return EntityMapper.toDto(getOrThrow(codesal));
    }

    public SalleDto creer(SalleDto dto) {
        String code = dto.getCodesal().trim();
        if (salleRepository.existsById(code)) {
            throw new BusinessException("Une salle avec le code " + code + " existe déjà.");
        }
        return EntityMapper.toDto(salleRepository.save(EntityMapper.toEntity(dto)));
    }

    public SalleDto modifier(String codesal, SalleDto dto) {
        Salle existing = getOrThrow(codesal);
        EntityMapper.updateEntity(existing, dto);
        return EntityMapper.toDto(salleRepository.save(existing));
    }

    public void supprimer(String codesal) {
        getOrThrow(codesal);
        if (occuperRepository.existsBySalle_Codesal(codesal)) {
            throw new BusinessException("Impossible de supprimer cette salle : elle a des occupations enregistrées.");
        }
        salleRepository.deleteById(codesal);
    }

    public Salle getOrThrow(String codesal) {
        return salleRepository.findById(codesal)
                .orElseThrow(() -> new ResourceNotFoundException("Salle introuvable : " + codesal));
    }
}
