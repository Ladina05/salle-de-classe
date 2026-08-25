package com.gestion.salles.controller;

import com.gestion.salles.dto.SalleDto;
import com.gestion.salles.service.SalleService;
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
@RequestMapping("/api/salles")
public class SalleController {

    private final SalleService salleService;

    public SalleController(SalleService salleService) {
        this.salleService = salleService;
    }

    @GetMapping
    public List<SalleDto> lister() {
        return salleService.lister();
    }

    @GetMapping("/{codesal}")
    public SalleDto trouver(@PathVariable String codesal) {
        return salleService.trouver(codesal);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SalleDto creer(@Valid @RequestBody SalleDto dto) {
        return salleService.creer(dto);
    }

    @PutMapping("/{codesal}")
    public SalleDto modifier(@PathVariable String codesal, @Valid @RequestBody SalleDto dto) {
        return salleService.modifier(codesal, dto);
    }

    @DeleteMapping("/{codesal}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable String codesal) {
        salleService.supprimer(codesal);
    }
}
