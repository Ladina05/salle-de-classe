package com.gestion.salles.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(
        name = "occuper",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_occuper_prof_salle_date", columnNames = {"codeprof", "codesal", "date"}),
                @UniqueConstraint(name = "uk_occuper_salle_date", columnNames = {"codesal", "date"})
        }
)
public class Occuper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "codeprof", referencedColumnName = "codeprof", nullable = false)
    @NotNull
    private Prof prof;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "codesal", referencedColumnName = "codesal", nullable = false)
    @NotNull
    private Salle salle;

    /** Correspond à l'attribut « date » du sujet. */
    @Column(name = "date", nullable = false)
    @NotNull(message = "La date d'occupation est obligatoire")
    private LocalDate date;

    public Occuper() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Prof getProf() {
        return prof;
    }

    public void setProf(Prof prof) {
        this.prof = prof;
    }

    public Salle getSalle() {
        return salle;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
