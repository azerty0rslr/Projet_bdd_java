# Projet BDD x JAVA
## Partie autonomie du 16/03 - JPA
### Création de adapter-jpa
Sur le même principe que les autres sous-projets je reprends la même structure :  
adpater-jpa -> src -> main -> java  
  
Avec un ```build.gradle``` à sa source (idem j'ai repris la structure des sous-projets précédents) :  
```gradle
plugins {
    id 'org.springframework.boot' version '3.2.5'
    id 'io.spring.dependency-management' version '1.1.4'
    id 'java'
}
dependencies {
    implementation project(':adapter-jpa')
    implementation 'org.springframework.boot:spring-boot-starter'
    implementation 'org.springframework.boot:spring-boot-starter-data-mongodb'
}
```  
  
De plus j'ai décommenté ```implementation project(':adapter-jpa')``` du ```build.gradle``` de app.  
Ainsi que ```include 'adapter-jpa'``` du ```settings.gradle``` du projet.  
Pas de difficulté pour cette première partie.  
### Ajout du contenu de adapter-jpa
Ensuite dans le dossier java je créer le package ```com.example.jpa``` avec la classe Java ```DAOUserJpa```, l'interface ```UserJpaRepository``` et la classe ```UserJpa```.  
J'ai repris la même structure que pour le Mongo mais j'avais beaucoup d'erreurs que je ne comprennais pas, en relancant le lendemain, plus aucune erreur ne s'affichait.

## Suite du projet le 17/03 - JPA
### Les articles
Etant donné que l'énoncé nous parle d'articles, je créer dans ```core-domain``` la même structure que pour User mais cette fois ci pour les articles ```Article.java```, ```ArticleService.java``` et ```IDAOArticle.java```.

### Mise en place de l'architecture DDD multi-modules

L'objectif est de structurer le projet en respectant les principes du **Domain-Driven Design (DDD)** avec une séparation stricte des responsabilités entre les modules.

#### Structure finale du projet
```
projet_java
├── core-domain     → entités métier et interfaces (aucune dépendance externe)
├── adapter-jpa     → implémentation JPA des interfaces du domaine
└── app             → point d'entrée Spring Boot, controllers REST
```

#### Règles d'architecture respectées
- `core-domain` ne dépend d'aucun adapter ni d'aucun framework
- `adapter-jpa` dépend uniquement de `core-domain`
- `app` assemble tout et dépend de `core-domain` et `adapter-jpa`

---

### Contenu de core-domain

Trois classes constituant le cœur métier, sans aucune annotation Spring ou JPA :

- **`Article.java`** : classe métier avec les champs `id`, `titre` et `contenu`
- **`IDAOArticle.java`** : interface définissant les opérations CRUD (`findAll`, `findById`, `save`, `deleteById`)
- **`ArticleService.java`** : service métier qui utilise `IDAOArticle` via **injection par constructeur**, sans annotation Spring — Spring ne doit pas polluer le domaine

---

### Contenu de adapter-jpa

Implémentation de la persistance via Spring Data JPA :

- **`ArticleJpa.java`** : entité JPA annotée `@Entity`,映射 la table `articles` en base
- **`ArticleJpaRepository.java`** : interface étendant `JpaRepository<ArticleJpa, Long>`, gérée automatiquement par Spring Data
- **`DAOArticleJpa.java`** : classe annotée `@Repository` qui implémente `IDAOArticle` et fait le mapping entre `Article` (domaine) et `ArticleJpa` (persistance)

---

### Contenu de app

- **`Application.java`** : point d'entrée Spring Boot avec `@SpringBootApplication` et `scanBasePackages` couvrant `com.example.app` et `com.example.jpa`
- **`AppConfig.java`** : configuration Spring déclarant le bean `ArticleService` manuellement, avec `@EnableJpaRepositories` et `@EntityScan` pointant vers `com.example.jpa` pour que Spring détecte les repositories et entités du module `adapter-jpa`
- **`ArticleController.java`** : controller REST exposant les endpoints CRUD sur `/articles`

---

### Problèmes rencontrés et solutions

| Problème | Cause | Solution |
|---|---|---|
| `Error resolving plugin` sur `adapter-jpa` | Le plugin `org.springframework.boot` était redéclaré avec une version différente de celle de la racine | Supprimer tout le bloc `plugins {}` dans `adapter-jpa/build.gradle` |
| `No qualifying bean of type 'ArticleJpaRepository'` | Spring ne scannait pas le package `com.example.jpa` du module `adapter-jpa` | Ajouter `@EnableJpaRepositories` et `@EntityScan` dans `AppConfig.java` |
| `Port 8080 already in use` | Le service `PEMHTTPD-x64` (Parallels Endpoint Manager) occupait déjà le port 8080 | Changer le port applicatif à `8081` dans `application.yml` |

---

### Configuration Gradle

Point clé : le plugin `org.springframework.boot` est déclaré **une seule fois** à la racine avec `apply false`, puis activé uniquement dans `app`. Les modules `core-domain` et `adapter-jpa` héritent uniquement du `dependency-management` via le bloc `subprojects {}`.

---

### Test de l'API avec Postman

Une fois l'application démarrée sur `http://localhost:8081`, les endpoints suivants ont été testés :

| Méthode | URL | Description |
|---|---|---|
| `GET` | `/articles` | Récupère tous les articles |
| `GET` | `/articles/{id}` | Récupère un article par son ID |
| `POST` | `/articles` | Crée un nouvel article |
| `DELETE` | `/articles/{id}` | Supprime un article |

La console H2 est accessible sur `http://localhost:8081/h2-console` avec l'URL JDBC `jdbc:h2:mem:testdb`.

### Adaptation du modèle métier

L'énoncé du TP imposant un modèle précis, j'ai adapté les classes de `core-domain` :

- **`Article.java`** : modification des champs pour correspondre au BO :
  - `id` passe de `Long` à `String` généré automatiquement en **UUID** via `UUID.randomUUID().toString()` dans le constructeur par défaut
  - `titre` renommé en `title`
  - `contenu` renommé en `description`

- **`Response.java`** : classe générique de retour structuré avec trois champs :
  - `code` : code métier (ex: 2002, 2003, 7001, 7006)
  - `message` : message lisible décrivant le résultat
  - `data` : données retournées (peut être `null`)

- **`IDAOArticle.java`** : ajout de deux méthodes nécessaires à la logique métier :
  - `findByTitle(String title)` : pour détecter les titres en doublon
  - `existsById(String id)` : pour vérifier l'existence avant suppression ou mise à jour

---

### Logique métier dans ArticleService

`ArticleService` implémente les règles définies dans l'énoncé :

| Méthode | Comportement |
|---|---|
| `getAll()` | Retourne toujours `2002` avec la liste |
| `getById(id)` | `2002` si trouvé, `7001` si inexistant |
| `delete(id)` | `2002` + `true` si supprimé, `7001` + `false` si inexistant |
| `save(article)` | `2002` si création, `2003` si mise à jour, `7006` si titre déjà utilisé |

La logique de `save` distingue création et mise à jour en vérifiant si l'`id` existe déjà en base. La détection de doublon de titre exclut l'article lui-même (utile lors d'une mise à jour sans changer le titre).

---

### Adaptation de adapter-jpa

Suite au changement de type de l'`id` (`Long` → `String`) :

- **`ArticleJpa.java`** : l'`id` n'est plus `@GeneratedValue` car la génération UUID se fait dans le domaine
- **`ArticleJpaRepository.java`** : le type générique passe de `JpaRepository<ArticleJpa, Long>` à `JpaRepository<ArticleJpa, String>`, et ajout de `findByTitle()` — Spring Data génère automatiquement la requête SQL à partir du nom de la méthode
- **`DAOArticleJpa.java`** : implémentation des deux nouvelles méthodes `findByTitle` et `existsById`

---

### Test final sur Postman

| Méthode | URL | Body | Réponse attendue |
|---|---|---|---|
| `GET` | `/articles` | — | `2002` + liste |
| `GET` | `/articles/{id}` | — | `2002` + article ou `7001` |
| `POST` | `/articles` | `{"title": "...", "description": "..."}` | `2002` si créé, `2003` si mis à jour, `7006` si titre doublon |
| `DELETE` | `/articles/{id}` | — | `2002` + `true` ou `7001` + `false` |

La structure de chaque réponse respecte le format imposé par l'énoncé :
```json
{
  "code": 2002,
  "message": "Article créé avec succès",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "title": "Mon premier article",
    "description": "Contenu de test"
  }
}
```  
### Ajout de adapter-mongo

Création du module `adapter-mongo` sur le même principe que `adapter-jpa` avec trois fichiers dans `com.example.mongo` :

- **`ArticleMongo.java`** : document MongoDB annoté `@Document(collection = "articles")` 
  avec un `@Id` de type `String`
- **`ArticleMongoRepository.java`** : interface étendant `MongoRepository<ArticleMongo, String>` 
  avec `findByTitle()` pour la détection de doublons
- **`DAOArticleMongo.java`** : implémente `IDAOArticle` avec la même logique de mapping 
  que `DAOArticleJpa` mais vers MongoDB

#### Fichiers de configuration mis à jour

- **`settings.gradle`** : ajout de `include 'adapter-mongo'`
- **`app/build.gradle`** : ajout de `implementation project(':adapter-mongo')` et 
  `implementation 'org.springframework.boot:spring-boot-starter-data-mongodb'`
- **`AppConfig.java`** : ajout de `@EnableMongoRepositories(basePackages = "com.example.mongo")`
- **`Application.java`** : ajout de `"com.example.mongo"` dans `scanBasePackages`
- **`application.yml`** : ajout de la configuration MongoDB :
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/projet_java
```

#### Création de la base MongoDB
On commence par la création de la DB sur MongoDB :  
<img width="808" height="650" alt="image" src="https://github.com/user-attachments/assets/e6f2b45e-9a1a-4131-8e0b-9a5078ccc442" />  

> **Note :** Il n'est pas nécessaire de créer la collection manuellement — 
> Spring Data MongoDB la crée automatiquement au premier appel.

#### Basculer entre JPA et MongoDB

Pour choisir quelle implémentation utiliser, deux approches sont possibles :

**Option 1 — `@Primary`** *(approche utilisée)*  
Annoter `@Primary` sur la DAO souhaitée. Spring injectera automatiquement 
cette implémentation partout où `IDAOArticle` est demandé.

- MongoDB actif → `@Primary` sur `DAOArticleMongo`
- JPA actif → `@Primary` sur `DAOArticleJpa`

**Option 2 — Injection explicite dans `AppConfig`**  
Spécifier directement le type concret dans le bean `articleService` :
```java
// Pour MongoDB
@Bean
public ArticleService articleService(DAOArticleMongo idaoArticle) {
    return new ArticleService(idaoArticle);
}

// Pour JPA
@Bean
public ArticleService articleService(DAOArticleJpa idaoArticle) {
    return new ArticleService(idaoArticle);
}
```

C'est l'option 2 qui a été retenue car elle est plus explicite et évite 
les ambiguïtés de résolution de bean par Spring.  
Voici le résultat :  
<img width="1081" height="891" alt="image" src="https://github.com/user-attachments/assets/3c50f666-fab2-4e2a-bbea-1395a8a33aaf" />  
<img width="1068" height="460" alt="image" src="https://github.com/user-attachments/assets/58125061-9b98-413e-af7f-9066f74e5ad3" />  


#### Problèmes rencontrés

| Problème | Cause | Solution |
|---|---|---|
| Hibernate utilisé à la place de MongoDB malgré `@Primary` | Spring résolvait l'ambiguïté via le nom du paramètre dans `AppConfig` | Remplacer `IDAOArticle` par `DAOArticleMongo` explicitement dans `AppConfig` |
| `Bean of type 'DAOArticleMongo' could not be found` | `com.example.mongo` absent de `scanBasePackages` | Ajouter `"com.example.mongo"` dans `@SpringBootApplication` |
