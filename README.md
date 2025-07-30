# Exam - Application de Gestion de Tickets

## Description

Cette application Spring Boot implémente un système de gestion de tickets selon les spécifications du diagramme UML fourni. Elle permet aux utilisateurs de créer, visualiser et résoudre des tickets avec différents niveaux de priorité et catégories.

## Fonctionnalités

### Spécifications Fonctionnelles Implémentées

✅ **Création de ticket** : Un administrateur ou un non-administrateur peut créer un ticket
✅ **Résolution de ticket** : Un administrateur peut marquer un ticket comme résolu
✅ **Visualisation des tickets** :
- Un administrateur ou un non-administrateur peut voir la liste de tous les tickets
- Une personne non connectée peut voir la liste des tickets non résolus
✅ **Authentification** : Une personne se connecte via son pseudo et un mot de passe

### Spécifications Techniques Implémentées

✅ **Tests** : 2 tests unitaires et 2 tests d'intégration
✅ **Sécurité des données** : Les mots de passe sont hachés en base de données
✅ **Validation des données** : Les données sont validées avant leur persistance
✅ **Contrôle d'accès** : Les routes sont protégées selon les droits des utilisateurs

## Architecture

### Modèles (Entités)

- **User** : Utilisateur avec pseudo, mot de passe et statut admin
- **Ticket** : Ticket avec titre, description, statut résolu et relations
- **Priority** : Priorité du ticket (Basse, Normale, Haute, Critique)
- **Category** : Catégorie du ticket (Bug, Amélioration, Demande, etc.)

### Relations UML Implémentées

- **User ↔ Ticket** : Un utilisateur peut soumettre et résoudre plusieurs tickets
- **Ticket ↔ Priority** : Un ticket a exactement une priorité
- **Ticket ↔ Category** : Un ticket peut avoir plusieurs catégories

## API Endpoints

### Authentification
- `POST /api/auth/login` - Connexion utilisateur

### Utilisateurs
- `POST /api/users/register` - Créer un nouvel utilisateur
- `GET /api/users` - Récupérer tous les utilisateurs
- `GET /api/users/{id}` - Récupérer un utilisateur par ID

### Tickets
- `GET /api/tickets` - Récupérer tous les tickets (connecté) ou tickets non résolus (non connecté)
- `POST /api/tickets` - Créer un nouveau ticket
- `GET /api/tickets/{id}` - Récupérer un ticket par ID
- `PUT /api/tickets/{id}/resolve` - Marquer un ticket comme résolu (admin seulement)

### Priorités
- `GET /api/priorities` - Récupérer toutes les priorités
- `POST /api/priorities` - Créer une nouvelle priorité
- `GET /api/priorities/{id}` - Récupérer une priorité par ID

### Catégories
- `GET /api/categories` - Récupérer toutes les catégories
- `POST /api/categories` - Créer une nouvelle catégorie
- `GET /api/categories/{id}` - Récupérer une catégorie par ID

## Installation et Démarrage

### Prérequis
- Java 17
- Maven
- MySQL

### Configuration

1. **Variables d'environnement** (créer un fichier `.env`) :
```env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=exam_db
DB_USER=root
DB_PASSWORD=your_password
JWT_SECRET=your_jwt_secret_key
```

2. **Lancer l'application** :
```bash
./mvnw spring-boot:run
```

3. **Accéder à la documentation API** :
```
http://localhost:8080/swagger-ui/index.html
```

### Données de Test

L'application inclut des données de test automatiquement chargées :

**Utilisateurs** (mot de passe : "password") :
- `admin` (administrateur)
- `user1`, `user2` (utilisateurs normaux)
- `support` (administrateur)

**Priorités** : Basse, Normale, Haute, Critique
**Catégories** : Bug, Amélioration, Demande, Support, Sécurité

## Tests

### Tests Unitaires
- `TicketUnitTest` : Tests de création et validation des tickets
- `UserUnitTest` : Tests de création et gestion des utilisateurs

### Tests d'Intégration
- `TicketApiIntegrationTest` : Tests des endpoints API des tickets
- `UserApiIntegrationTest` : Tests des endpoints API des utilisateurs

### Exécuter les Tests
```bash
./mvnw test
```

## Sécurité

- **Authentification JWT** : Tokens JWT pour l'authentification
- **Hachage des mots de passe** : BCrypt pour sécuriser les mots de passe
- **Contrôle d'accès** : Rôles ADMIN et USER avec permissions appropriées
- **Validation des données** : Validation côté serveur avec Bean Validation

## Conteneurisation

### Docker
```bash
# Construire l'image
docker build -t exam .

# Lancer avec docker-compose
docker-compose up
```

### Docker Compose
Le fichier `docker-compose.yml` inclut :
- Application Spring Boot (exam-app)
- Base de données MySQL (exam-db)
- phpMyAdmin (exam-pma)
- Configuration réseau

## Technologies Utilisées

- **Spring Boot 3.5.0**
- **Spring Security** avec JWT
- **Spring Data JPA** avec Hibernate
- **MySQL** pour la base de données
- **Lombok** pour réduire le code boilerplate
- **SpringDoc OpenAPI** pour la documentation API
- **JUnit 5** pour les tests
- **Maven** pour la gestion des dépendances



