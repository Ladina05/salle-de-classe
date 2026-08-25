package com.gestion.salles.controller;

import com.gestion.salles.dto.OccupantDto;
import com.gestion.salles.service.OccupantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/occuper")
public class OccupantController {

    private final OccupantService occupantService;

    public OccupantController(OccupantService occupantService) {
        this.occupantService = occupantService;
    }

    @GetMapping
    public List<OccupantDto> lister() {
        return occupantService.lister();
    }

    @GetMapping("/{id}")
    public OccupantDto trouver(@PathVariable Long id) {
        return occupantService.trouver(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OccupantDto creer(@Valid @RequestBody OccupantDto dto) {
        return occupantService.creer(dto);
    }

    @PutMapping("/{id}")
    public OccupantDto modifier(@PathVariable Long id, @Valid @RequestBody OccupantDto dto) {
        return occupantService.modifier(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        occupantService.supprimer(id);
    }
}
