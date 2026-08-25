package com.gestion.salles.repository;

import com.gestion.salles.entity.Occuper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OccuperRepository extends JpaRepository<Occuper, Long> {

    @Query("SELECT o FROM Occuper o JOIN FETCH o.prof JOIN FETCH o.salle ORDER BY o.date DESC, o.id DESC")
    List<Occuper> findAllWithRelations();

    @Query("SELECT o FROM Occuper o JOIN FETCH o.prof JOIN FETCH o.salle WHERE o.id = :id")
    Optional<Occuper> findByIdWithRelations(Long id);

    boolean existsBySalle_CodesalAndDateAndIdNot(String codesal, LocalDate date, Long id);

    boolean existsBySalle_CodesalAndDate(String codesal, LocalDate date);

    boolean existsByProf_Codeprof(String codeprof);

    boolean existsBySalle_Codesal(String codesal);

    boolean existsByProf_CodeprofAndSalle_CodesalAndDate(String codeprof, String codesal, LocalDate date);
}
