# Gestion des salles de classe

Application complète : **API Spring Boot** (ORM JPA/Hibernate → **PostgreSQL / pgAdmin 4**) + **client desktop Java Swing**.

## Architecture

```
Client Java Swing  ──HTTP REST──►  Spring Boot (port 8080)  ──JPA──►  PostgreSQL
     CRUD + recherche profs              Entités PROF / SALLE / OCCUPER
```

| Table     | Attributs                          | Types Java / PostgreSQL        |
|-----------|------------------------------------|--------------------------------|
| PROF      | codeprof, nom, prenom, grade       | `String` / `VARCHAR`           |
| SALLE     | codesal, designation               | `String` / `VARCHAR`           |
| OCCUPER   | codeprof, codesal, date (+ id)     | FK + `LocalDate` / `DATE`      |

Hibernate crée et met à jour les tables automatiquement (`ddl-auto=update`).

## 1. Prérequis

- **JDK 17** (ou plus)
- **Maven 3.9+**
- **PostgreSQL** + **pgAdmin 4**

## 2. Créer la base dans pgAdmin 4

1. Ouvrez pgAdmin 4 et connectez-vous au serveur (souvent `localhost`, utilisateur `postgres`).
2. Clic droit sur **Databases** → **Query Tool**.
3. Exécutez le fichier `sql/01_create_database.sql` (ou simplement) :

```sql
CREATE DATABASE gestion_salles WITH OWNER = postgres ENCODING = 'UTF8' TEMPLATE = template0;
```

4. Dans `server/src/main/resources/application.properties`, adaptez **utilisateur** et **mot de passe** PostgreSQL :

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/gestion_salles
spring.datasource.username=postgres
spring.datasource.password=postgres
```

5. Au premier démarrage de Spring Boot, les tables `prof`, `salle` et `occuper` sont créées par l’ORM.  
   Des données de démonstration sont insérées si la base est vide.

Le script `sql/02_tables.sql` est optionnel (création manuelle du schéma).  
`sql/03_donnees_demo.sql` sert si vous voulez charger les données vous-même.

## 3. Lancer l’API (serveur)

À la racine du projet :

```bash
mvn -pl server spring-boot:run
```

Ou, après compilation :

```bash
mvn -pl server package
java -jar server/target/server-1.0.0.jar
```

Vérification : [http://localhost:8080/api/profs](http://localhost:8080/api/profs)

## 4. Lancer le client Swing

Dans un **second** terminal (l’API doit déjà tourner) :

```bash
mvn -pl desktop package
java -jar desktop/target/desktop-1.0.0.jar
```

Onglets :

- **Professeurs** : CRUD + recherche par code ou nom
- **Salles** : CRUD
- **Occupations** : CRUD (un prof, une salle, une date) — une salle ne peut pas être occupée deux fois le même jour

## API REST

| Méthode | URL | Action |
|---------|-----|--------|
| GET | `/api/profs` | Liste |
| GET | `/api/profs/recherche?terme=...` | Recherche code / nom / prénom |
| GET | `/api/profs/{codeprof}` | Détail |
| POST | `/api/profs` | Créer |
| PUT | `/api/profs/{codeprof}` | Modifier |
| DELETE | `/api/profs/{codeprof}` | Supprimer |
| GET/POST | `/api/salles` | Liste / créer |
| PUT/DELETE | `/api/salles/{codesal}` | Modifier / supprimer |
| GET/POST | `/api/occuper` | Liste / créer |
| PUT/DELETE | `/api/occuper/{id}` | Modifier / supprimer |

## Compilation complète

```bash
mvn clean package
```
