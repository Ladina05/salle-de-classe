package com.gestion.salles.config;

import com.gestion.salles.entity.Occuper;
import com.gestion.salles.entity.Prof;
import com.gestion.salles.entity.Salle;
import com.gestion.salles.repository.OccuperRepository;
import com.gestion.salles.repository.ProfRepository;
import com.gestion.salles.repository.SalleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ProfRepository profRepository;
    private final SalleRepository salleRepository;
    private final OccuperRepository occuperRepository;

    public DataSeeder(ProfRepository profRepository,
                      SalleRepository salleRepository,
                      OccuperRepository occuperRepository) {
        this.profRepository = profRepository;
        this.salleRepository = salleRepository;
        this.occuperRepository = occuperRepository;
    }

    @Override
    public void run(String... args) {
        if (profRepository.count() > 0) {
            return;
        }

        Prof p1 = profRepository.save(new Prof("P001", "Rakoto", "Jean", "Professeur titulaire"));
        Prof p2 = profRepository.save(new Prof("P002", "Rasoanaivo", "Marie", "Maître de conférences"));
        Prof p3 = profRepository.save(new Prof("P003", "Andria", "Paul", "Assistant"));

        Salle s1 = salleRepository.save(new Salle("S101", "Salle 101 — Informatique"));
        Salle s2 = salleRepository.save(new Salle("S102", "Amphithéâtre A"));
        Salle s3 = salleRepository.save(new Salle("LAB1", "Laboratoire réseaux"));

        Occuper o1 = new Occuper();
        o1.setProf(p1);
        o1.setSalle(s1);
        o1.setDate(LocalDate.now());
        occuperRepository.save(o1);

        Occuper o2 = new Occuper();
        o2.setProf(p2);
        o2.setSalle(s2);
        o2.setDate(LocalDate.now().plusDays(1));
        occuperRepository.save(o2);

        Occuper o3 = new Occuper();
        o3.setProf(p3);
        o3.setSalle(s3);
        o3.setDate(LocalDate.now().plusDays(2));
        occuperRepository.save(o3);
    }
}
