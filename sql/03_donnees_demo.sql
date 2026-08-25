-- Données de démonstration (pgAdmin 4, base gestion_salles)

INSERT INTO prof (codeprof, nom, prenom, grade) VALUES
    ('P001', 'Rakoto', 'Jean', 'Professeur titulaire'),
    ('P002', 'Rasoanaivo', 'Marie', 'Maître de conférences'),
    ('P003', 'Andria', 'Paul', 'Assistant');

INSERT INTO salle (codesal, designation) VALUES
    ('S101', 'Salle 101 — Informatique'),
    ('S102', 'Amphithéâtre A'),
    ('LAB1', 'Laboratoire réseaux');

INSERT INTO occuper (codeprof, codesal, date) VALUES
    ('P001', 'S101', CURRENT_DATE),
    ('P002', 'S102', CURRENT_DATE + 1),
    ('P003', 'LAB1', CURRENT_DATE + 2);
