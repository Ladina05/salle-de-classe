-- À exécuter dans pgAdmin 4 :
-- 1. Connectez-vous au serveur PostgreSQL
-- 2. Clic droit sur « Databases » → Query Tool
-- 3. Exécutez ce script (F5)

CREATE DATABASE gestion_salles
    WITH OWNER = postgres
         ENCODING = 'UTF8'
         TEMPLATE = template0;

COMMENT ON DATABASE gestion_salles IS 'Gestion des salles de classe — projet Spring Boot + JPA';
