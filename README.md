# Eventia - Système de Gestion de Réservations d'Événements

<p align="center">
  <img src="https://github.com/user-attachments/assets/6f2b8834-2e18-4428-98dc-53e30b6d2e6a" alt="Eventia" width="400" height="800"/>
</p>

## 📋 Description du Projet

Eventia est une plateforme web complète de gestion de réservations d'événements culturels (concerts, théâtres, conférences, événements sportifs, etc.). L'application permet aux organisateurs de créer et gérer des événements, aux clients de réserver des places en ligne, et aux administrateurs de superviser l'ensemble de la plateforme.

### Fonctionnalités Principales

- **Gestion des Événements** : Création, modification, publication et annulation d'événements
- **Système de Réservation** : Réservation en ligne avec génération de code unique
- **Multi-rôles** : Interface adaptée pour Clients, Organisateurs et Administrateurs
- **Tableau de Bord Interactif** : Statistiques et graphiques en temps réel
- **Gestion des Utilisateurs** : Inscription, authentification et gestion de profil
- **Interface Moderne** : Design responsive avec Vaadin

## 🛠️ Technologies Utilisées

### Backend
- **Java 17+** - Langage de programmation
- **Spring Boot 3.x** - Framework principal
- **Spring Data JPA** - Persistance des données
- **Spring Security** - Authentification et autorisation
- **Hibernate** - ORM (Object-Relational Mapping)
- **H2 Database** - Base de données embarquée

### Frontend
- **Vaadin 24.x** - Framework UI Java
- **Vaadin Components** - Composants UI modernes
- **Vaadin Charts** - Graphiques et visualisations

### Build & Outils
- **Maven** - Gestion des dépendances
- **Lombok** - Réduction du code boilerplate
- **BCrypt** - Hashage des mots de passe

## 📦 Prérequis

Avant de commencer, assurez-vous d'avoir installé :

- **JDK 17 ou supérieur** - [Télécharger ici](https://www.oracle.com/java/technologies/downloads/)
- **Maven 3.8+** - [Télécharger ici](https://maven.apache.org/download.cgi)
- **Git** - [Télécharger ici](https://git-scm.com/downloads)
- **Un IDE Java** (recommandé) :
  - IntelliJ IDEA
  - Eclipse
  - VS Code avec extension Java

## 🚀 Installation

### 1. Cloner le Repository

```bash
git clone https://github.com/FatimaZahraBoukab/Eventia.git
cd Eventia
```

### 2. Installer les Dépendances

```bash
mvn clean install
```

## ⚙️ Configuration de la Base de Données

La base de données H2 est configurée en mode embedded et s'initialise automatiquement au démarrage de l'application.

### Configuration dans `application.properties`

```properties
# Configuration H2 Database
spring.datasource.url=jdbc:h2:mem:eventiadb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# Configuration JPA/Hibernate
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Console H2 (pour le développement)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Initialisation des données
spring.jpa.defer-datasource-initialization=true
spring.sql.init.mode=always
```

### Génération du Script SQL

Pour générer le script SQL de création des tables, ajoutez ces propriétés dans `application.properties` :

```properties
spring.jpa.properties.javax.persistence.schema-generation.scripts.action=create
spring.jpa.properties.javax.persistence.schema-generation.scripts.create-target=schema.sql
spring.jpa.properties.javax.persistence.schema-generation.scripts.create-source=metadata
```

Le fichier `schema.sql` sera automatiquement généré à la racine du projet au démarrage de l'application.

## ▶️ Instructions de Lancement

### Méthode 1 : Avec Maven

```bash
mvn spring-boot:run
```

### Méthode 2 : Depuis votre IDE

Exécutez la classe principale `EventiaApplication.java`


## 🌐 Accès à l'Application

Une fois l'application lancée, accédez à :

- **Application Web** : [http://localhost:8080](http://localhost:8080)
- **Console H2** : [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
  - JDBC URL : `jdbc:h2:mem:eventiadb`
  - Username : `sa`
  - Password : (laisser vide)

## 🏗️ Architecture du Projet

```
eventia/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── eventmanagement/
│   │   │           └── eventreservation/
│   │   │               ├── config/
│   │   │               │   ├── SecurityConfig.java
│   │   │               ├── controller
│   │   │               │   └──AuthController.java
│   │   │               ├── entity/
│   │   │               │   ├── ContactMessage.java
│   │   │               │   ├── Event.java
│   │   │               │   ├── EventCategory.java
│   │   │               │   ├── EventStatus.java
│   │   │               │   ├── Reservation.java
│   │   │               │   ├── ReservationStatus.java
│   │   │               │   ├── Role.java
│   │   │               │   └── User.java
│   │   │               ├── repository/
│   │   │               │   ├── ContactMessageRepository.java
│   │   │               │   ├── EventRepository.java
│   │   │               │   ├── ReservationRepository.java
│   │   │               │   └── UserRepository.java
│   │   │               ├── service/
│   │   │               │   ├── ContactMessageService.java
│   │   │               │   ├── EventService.java
│   │   │               │   ├── ReservationService.java
│   │   │               │   └── UserService.java
│   │   │               ├── views/
│   │   │               │   ├── admin/
│   │   │               │   │   ├── AdminDashboardView.java
│   │   │               │   │   ├── AdminEventsView.java
│   │   │               │   │   ├── AdminInboxView.java
│   │   │               │   │   ├── AdminProfileView.java
│   │   │               │   │   ├── AdminReservationView.java
│   │   │               │   │   ├── AdminSidebar.java
│   │   │               │   │   └── AdminUsersView.java
│   │   │               │   ├── client/
│   │   │               │   │   ├── ClientDashboardView.java
│   │   │               │   │   ├── ClientEventsView.java
│   │   │               │   │   ├── ClientProfileView.java
│   │   │               │   │   ├── ClientReservation.java
│   │   │               │   │   ├── ClientSidebar.java
│   │   │               │   │   ├── ClientSupportView.java
│   │   │               │   │   └── ReservationFormView.java
│   │   │               │   ├── components/
│   │   │               │   │   ├── AboutSection.java
│   │   │               │   │   ├── AvisSection.java
│   │   │               │   │   ├── ContactSection.java
│   │   │               │   │   ├── Footer.java
│   │   │               │   │   ├── HeroSection.java
│   │   │               │   │   ├── PublicHeader.java
│   │   │               │   │   └── ServicesSection.java
│   │   │               │   ├── organizer/
│   │   │               │   │   ├── EventForm.java
│   │   │               │   │   ├── OrganizerDashboardView.java
│   │   │               │   │   ├── OrganizerEventsView.java
│   │   │               │   │   ├── OrganizerProfileView.java
│   │   │               │   │   ├── OrganizerReservationsView.java
│   │   │               │   │   └── OrganizerSidebar.java
│   │   │               │   ├── LoginView.java
│   │   │               │   ├── MainView.java  
│   │   │               │   ├── RegisterView.java
│   │   │               │   └── TousEvenements.java
│   │   │               │   
│   │   │               └── EventsReservationApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── data.sql
│   │       └── static/
│   │           └── images/
│   └── test/
│       └── java/
├── target/
├── pom.xml
└── README.md
```

## 🔑 Règles Métier Importantes

- Une réservation ne peut pas dépasser **10 places**
- Les réservations peuvent être annulées jusqu'à **48h avant l'événement**
- Le nombre total de places réservées ne peut pas dépasser la **capacité maximale**
- Un événement terminé ne peut plus être modifié
- Un événement ne peut être publié que s'il contient toutes les informations requises
- Le code de réservation est unique au format **EVT-XXXXX**
- Le montant total est calculé automatiquement : **nombre de places × prix unitaire**


## 📊 Concepts Java Avancés Utilisés

- **Streams API** : Filtrage, transformation et calcul de statistiques sur les collections
- **Optional** : Gestion élégante des valeurs nulles pour éviter les NullPointerException
- **Lambda Expressions** : Utilisation dans les listeners Vaadin et les comparateurs
- **Generics** : Classes et méthodes génériques pour la réutilisabilité du code
- **Enums avec méthodes** : Logique métier intégrée dans les énumérations
- **Design Patterns** : 
  - Singleton pour les services utilitaires
  - Factory pour la création de composants Vaadin
  - Observer pour les écouteurs d'événements

## 📄 Licence

Ce projet est développé dans le cadre d'un mini-projet académique - Spring Boot & Vaadin.

## 👥 Auteur

**BOUKAB fatima Zahra** - Développement complet du système de gestion de réservations d'événements

## 📞 Contact

Pour toute question ou suggestion :
- Email : fatimazahraboukab9@gmail.com
          boukab.fatimazahra@etu.uae.ac.ma
- GitHub : [@FatimaZahraBoukab](https://github.com/FatimaZahraBoukab)

