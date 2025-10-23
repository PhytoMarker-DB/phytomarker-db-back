# PhytoMarker-DB Backend 🌿

Bienvenue sur le dépôt du back-end de **PhytoMarker-DB**. Cette application, développée avec **Spring Boot**, sert d'API RESTful pour le projet. Elle est responsable de la persistance des données, de la logique métier et de l'exposition sécurisée des informations à l'application cliente.

Ce dépôt contient uniquement le code de l'API serveur. Le code du front-end (Angular) se trouve dans le dépôt [phytomarker-db-front](https://github.com/PhytoMarker-DB/phytomarker-db-front).

<img width="1900" height="941" alt="screely-1761208226855" src="https://github.com/user-attachments/assets/f69860b7-1f95-4ca1-8f4a-870eac9d85bf" />


## ✨ Fonctionnalités Principales

*   **API RESTful Complète :** Exposition de endpoints clairs et structurés pour toutes les opérations CRUD (Create, Read, Update, Delete) sur les entités de l'application.
*   **Recherche Multi-Critères :** Un endpoint de recherche puissant (`/api/plants/search`) utilisant JPA Specifications pour permettre des requêtes complexes et performantes.
*   **Logique de Pedigree Côté Serveur :** Un endpoint dédié (`/api/plants/{id}/pedigree`) qui reconstruit l'ascendance complète d'une plante de manière récursive et optimisée.
*   **Gestion des Données :** Persistance des plantes, marqueurs génétiques, génotypes et observations phénotypiques dans une base de données relationnelle.
*   **Export de Données :** Un endpoint pour générer et exporter les résultats de recherche au format CSV.
*   **Sécurité :** Une couche de sécurité de base gérée par Spring Security, avec une configuration CORS pour autoriser les requêtes depuis le front-end.
*   **Initialisation des Données :** Un script d'initialisation (`DataInitializer`) pour peupler la base de données avec un jeu de données de test cohérent au premier lancement en mode développement.

## 🛠️ Stack Technique

*   **Framework :** [Spring Boot](https://spring.io/projects/spring-boot) v3.x
*   **Langage :** [Java](https://www.java.com/) 21
*   **Accès aux Données :** Spring Data JPA / [Hibernate](https://hibernate.org/)
*   **Base de Données :** [PostgreSQL](https://www.postgresql.org/)
*   **Sécurité :** [Spring Security](https://spring.io/projects/spring-security)
*   **Gestion des Dépendances :** [Maven](https://maven.apache.org/)

## 🚀 Démarrage Rapide

Pour lancer l'application en local, suivez ces étapes.

### Prérequis

Assurez-vous d'avoir les outils suivants installés sur votre machine :
*   JDK 21 (ou supérieur)
*   Maven 3.8+
*   PostgreSQL (version 12 ou supérieure recommandée)
*   Un IDE comme IntelliJ IDEA ou VS Code avec les extensions Java/Spring.

### Installation et Configuration

1.  **Clonez le dépôt :**
    ```bash
    git clone https://github.com/PhytoMarker-DB/phytomarker-db-back.git
    cd phytomarker-db-back
    ```

2.  **Configuration de la Base de Données :**
    *   Lancez votre service PostgreSQL.
    *   Créez une nouvelle base de données. Le nom par défaut attendu est `phytomarkerdb`.
        ```sql
        CREATE DATABASE phytomarkerdb;
        ```    *   Ouvrez le fichier de configuration de l'application : `src/main/resources/application.yml`.
    *   Modifiez les informations de connexion si nécessaire pour correspondre à votre configuration locale :
        ```yaml
        spring:
          datasource:
            url: jdbc:postgresql://localhost:5432/phytomarkerdb
            username: postgres # Votre nom d'utilisateur PostgreSQL
            password: root     # Votre mot de passe
        ```

3.  **Configuration de la Génération du Schéma :**
    Dans `application.yml`, la propriété `spring.jpa.hibernate.ddl-auto` est configurée sur `create`.
    > **⚠️ Attention :** Ce mode **détruit et recrée** toutes les tables à chaque redémarrage de l'application. C'est idéal pour le développement mais **ne doit jamais être utilisé en production**.

### Lancement

Vous pouvez lancer l'application de plusieurs manières :

*   **Via Maven (recommandé) :**
    ```bash
    mvn spring-boot:run
    ```

*   **Via votre IDE :**
    Ouvrez le projet en tant que projet Maven, puis exécutez la méthode `main` de la classe `PhytomarkerDbApplication.java`.

Une fois lancée, l'application sera accessible sur `http://localhost:8080`.

## 📖 Endpoints de l'API

Voici une liste des principaux endpoints publics disponibles :

| Méthode HTTP | Endpoint                            | Description                                                                                             |
|--------------|-------------------------------------|---------------------------------------------------------------------------------------------------------|
| `GET`        | `/api/plants/search`                | Recherche des plantes avec des filtres ( `variety`, `minMildewScore`, `markerNames` en query params). |
| `GET`        | `/api/plants/{id}`                  | Récupère les détails complets d'une seule plante par son ID.                                            |
| `GET`        | `/api/plants/{id}/pedigree`         | Récupère l'ascendance complète d'une plante pour la visualisation du pedigree.                          |
| `GET`        | `/api/plants/varieties`             | Retourne une liste unique et triée de toutes les variétés de plantes disponibles.                       |
| `GET`        | `/api/markers`                      | Retourne une liste unique et triée de tous les noms de marqueurs disponibles.                           |
| `POST`       | `/api/plants`                       | Crée une nouvelle plante. Le corps de la requête doit contenir un JSON représentant la plante.         |

## 📁 Structure du Projet

Le projet suit une architecture en couches classique pour une application Spring Boot.

```
src/main/java/fr/cda/phytomarker_db/
├── config/                   # Configuration de l'application (Sécurité, etc.)
├── controller/               # Contrôleurs REST qui exposent les endpoints de l'API
├── dto/                      # Data Transfer Objects pour formater les données de l'API
├── exception/                # Gestion des exceptions personnalisées
├── init/                     # Scripts d'initialisation des données pour le développement
├── model/                    # Entités JPA qui représentent le schéma de la base de données
├── repository/               # Interfaces Spring Data JPA pour l'accès aux données
└── service/                  # Couche de service contenant la logique métier
```

## 🔒 Sécurité

La sécurité est gérée par **Spring Security**. La configuration actuelle (`SecurityConfig.java`) est simple :
*   **CORS** est configuré pour accepter les requêtes provenant de `http://localhost:4200` (l'application Angular).
*   **Permissions :**
    *   Toutes les requêtes `GET` sont publiques et ne nécessitent pas d'authentification.
    *   Toutes les requêtes `POST` sur `/api/plants` sont publiques pour permettre la saisie de données.
    *   Toutes les autres requêtes (`PUT`, `DELETE`, etc.) sont protégées et nécessitent une authentification (actuellement configurée en HTTP Basic avec un utilisateur en mémoire).

---

### Pedigree et Détail Descendants 
<img width="1917" height="939" alt="screely-1761211437329" src="https://github.com/user-attachments/assets/7fcc2fe6-6e42-402b-8170-a8a1720b5e08" />

### Formulaire de saisie
<img width="1917" height="942" alt="screely-1761211552445" src="https://github.com/user-attachments/assets/b179194d-7faa-4548-95ec-54e300218468" />



