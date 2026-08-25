package com.gestion.salles.repository;

import com.gestion.salles.entity.Salle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalleRepository extends JpaRepository<Salle, String> {
}
