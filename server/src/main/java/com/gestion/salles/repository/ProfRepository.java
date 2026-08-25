package com.gestion.salles.repository;

import com.gestion.salles.entity.Prof;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProfRepository extends JpaRepository<Prof, String> {

    List<Prof> findByNomContainingIgnoreCase(String nom);

    @Query("""
            SELECT p FROM Prof p
            WHERE LOWER(p.codeprof) LIKE LOWER(CONCAT('%', :terme, '%'))
               OR LOWER(p.nom) LIKE LOWER(CONCAT('%', :terme, '%'))
               OR LOWER(p.prenom) LIKE LOWER(CONCAT('%', :terme, '%'))
            ORDER BY p.nom, p.prenom
            """)
    List<Prof> rechercherParCodeOuNom(@Param("terme") String terme);
}
