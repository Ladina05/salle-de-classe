-- Script optionnel : Hibernate (ORM) crée déjà ces tables au démarrage
-- (spring.jpa.hibernate.ddl-auto=update).
-- Utile si vous préférez créer le schéma à la main dans pgAdmin 4.
--
-- Dans pgAdmin : sélectionnez la base « gestion_salles », puis Query Tool.

DROP TABLE IF EXISTS occuper;
DROP TABLE IF EXISTS salle;
DROP TABLE IF EXISTS prof;

CREATE TABLE prof (
    codeprof   VARCHAR(20)  PRIMARY KEY,
    nom        VARCHAR(80)  NOT NULL,
    prenom     VARCHAR(80)  NOT NULL,
    grade      VARCHAR(60)  NOT NULL
);

CREATE TABLE salle (
    codesal      VARCHAR(20)  PRIMARY KEY,
    designation  VARCHAR(120) NOT NULL
);

CREATE TABLE occuper (
    id     BIGSERIAL PRIMARY KEY,
    codeprof VARCHAR(20) NOT NULL REFERENCES prof(codeprof),
    codesal  VARCHAR(20) NOT NULL REFERENCES salle(codesal),
    date     DATE        NOT NULL,
    CONSTRAINT uk_occuper_prof_salle_date UNIQUE (codeprof, codesal, date),
    CONSTRAINT uk_occuper_salle_date UNIQUE (codesal, date)
);

CREATE INDEX idx_occuper_codeprof ON occuper (codeprof);
CREATE INDEX idx_occuper_codesal ON occuper (codesal);
