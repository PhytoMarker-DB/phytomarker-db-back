# PhytoMarker-DB : Base de Données pour l'Association Génotype-Phénotype

PhytoMarker-DB est une application web full-stack conçue pour les généticiens, les chercheurs et les techniciens en amélioration des plantes. Elle offre une plateforme centralisée et sécurisée pour stocker, gérer et analyser des données de phénotypage et de génotypage, dans le but de découvrir des corrélations et d'accélérer les programmes de sélection variétale.

L'application met l'accent sur un moteur de recherche puissant, des outils de visualisation de données (tels que les arbres généalogiques) et un système d'accès sécurisé basé sur les rôles.


*(Remplacez ce lien par une capture d'écran de votre application, par exemple la page de recherche ou de résultats)*

---

## 🚀 Fonctionnalités Principales

*   **Moteur de Recherche Avancée** : Permet de construire des requêtes complexes en combinant de multiples critères (variété de plante, score d'un trait, présence/absence de marqueurs génétiques) pour filtrer les données avec une grande précision.
*   **Visualisation de Données Génétiques** : Génération de diagrammes de pedigree interactifs pour visualiser et comprendre la lignée génétique d'une plante.
*   **Centralisation des Données** : Stockage de toutes les informations relatives aux plantes, aux observations phénotypiques et aux résultats de génotypage dans une base de données relationnelle unique et structurée, garantissant la cohérence et l'intégrité des données.
*   **Sécurité et Gestion des Droits (RBAC)** : Un système d'accès basé sur les rôles (Administrateur, Chercheur, Technicien) assure que les utilisateurs ne peuvent accéder et modifier que les données autorisées par leurs permissions.
*   **Export des Données** : Possibilité d'exporter les résultats de recherche au format CSV pour des analyses plus poussées dans des logiciels tiers (R, Excel, etc.).

---

## 🏛️ Architecture et Stack Technique

Le projet est construit sur une **architecture N-Tiers** moderne, garantissant une séparation claire des responsabilités entre le client, le serveur et la base de données, ce qui favorise la maintenabilité, la sécurité et l'évolutivité.

### **Backend (API)**

L'API RESTful est le cœur logique de l'application, développé en suivant le patron de conception **Controller-Service-Repository**.

*   **Langage** : [Java 17](https://www.oracle.com/java/technologies/javase/17-relnote-license-faq.html)
*   **Framework** : [Spring Boot 3](https://spring.io/projects/spring-boot)
*   **Persistance des Données** : [Spring Data JPA](https://spring.io/projects/spring-data-jpa) pour l'accès aux données, avec l'utilisation de l'API **`Specification`** pour la construction de requêtes dynamiques et sécurisées.
*   **Base de Données** : [PostgreSQL](https://www.postgresql.org/)
*   **Sécurité** : [Spring Security](https://spring.io/projects/spring-security) pour la gestion de l'authentification (via **JSON Web Tokens - JWT**) et des autorisations au niveau des endpoints et des méthodes de service (`@PreAuthorize`).
*   **Gestion de Projet** : [Maven](https://maven.apache.org/)

### **Frontend (Client)**

L'interface utilisateur est développée en tant que **Single Page Application (SPA)** pour une expérience utilisateur fluide et réactive.

*   **Framework** : [Angular 16+](https://angular.io/)
*   **Langage** : [TypeScript](https://www.typescriptlang.org/)
*   **Mise en Page et Style** : [Tailwind CSS](https://tailwindcss.com/) pour une approche "utility-first" et un design entièrement responsive.
*   **Visualisation de Données** : [D3.js](https://d3js.org/) pour la génération des diagrammes de pedigree SVG.

---

## ⚙️ Installation et Démarrage

### **Prérequis**

*   Java JDK 17+
*   Maven 3.8+
*   Node.js 18+ et npm
*   Une instance de PostgreSQL en cours d'exécution
*   Angular CLI (`npm install -g @angular/cli`)

### **1. Configuration du Backend**

1.  **Cloner le dépôt :**
    ```bash
    git clone https://github.com/votre-pseudo/phytomarker-db-backend.git
    cd phytomarker-db-backend
    ```
2.  **Configurer la base de données :**
    Ouvrez le fichier `src/main/resources/application.properties` et mettez à jour les propriétés `spring.datasource` avec vos informations de connexion à PostgreSQL.
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/phytomarkerdb
    spring.datasource.username=votre_utilisateur_db
    spring.datasource.password=votre_mot_de_passe_db
    ```
3.  **Lancer l'application :**
    ```bash
    mvn spring-boot:run
    ```
    L'API backend sera accessible à l'adresse `http://localhost:8080`.

### **2. Configuration du Frontend**

1.  **Cloner le dépôt :**
    ```bash
    git clone https://github.com/votre-pseudo/phytomarker-db-frontend.git
    cd phytomarker-db-frontend
    ```
2.  **Installer les dépendances :**
    ```bash
    npm install
    ```
3.  **Lancer l'application :**
    ```bash
    ng serve
    ```
    L'interface utilisateur sera disponible à l'adresse `http://localhost:4200`.

---

## 📜 Endpoints API (Principaux)

*   `POST /api/auth/login`: Authentifier un utilisateur et obtenir un token JWT.
*   `GET /api/plants/search`: Effectuer une recherche avancée de plantes (accepte plusieurs paramètres de requête).
*   `GET /api/plants/{id}`: Obtenir les informations détaillées d'une plante spécifique.
*   `POST /api/observations`: Ajouter une nouvelle observation phénotypique.

---

## ✍️ Auteur

*   **[Votre Nom]** - [Lien vers votre profil GitHub ou portfolio]

Ce projet a été développé dans le cadre de ma certification pour le titre professionnel **Concepteur Développeur d'Applications**.