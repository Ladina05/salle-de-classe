package com.gestion.salles.controller;

import com.gestion.salles.dto.ProfDto;
import com.gestion.salles.service.ProfService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/profs")
public class ProfController {

    private final ProfService profService;

    public ProfController(ProfService profService) {
        this.profService = profService;
    }

    @GetMapping
    public List<ProfDto> lister() {
        return profService.lister();
    }

    @GetMapping("/recherche")
    public List<ProfDto> rechercher(@RequestParam(required = false) String terme,
                                    @RequestParam(required = false) String code,
                                    @RequestParam(required = false) String nom) {
        String query = terme;
        if (query == null || query.isBlank()) {
            query = code != null && !code.isBlank() ? code : nom;
        }
        return profService.rechercher(query);
    }

    @GetMapping("/{codeprof}")
    public ProfDto trouver(@PathVariable String codeprof) {
        return profService.trouver(codeprof);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProfDto creer(@Valid @RequestBody ProfDto dto) {
        return profService.creer(dto);
    }

    @PutMapping("/{codeprof}")
    public ProfDto modifier(@PathVariable String codeprof, @Valid @RequestBody ProfDto dto) {
        return profService.modifier(codeprof, dto);
    }

    @DeleteMapping("/{codeprof}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable String codeprof) {
        profService.supprimer(codeprof);
    }
}
