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
Ensuite dans le dossier java je créer le package ```com.example.jpa``` avec la classe Java ```DAOArticleJpa```, l'interface ```ArticleJpaRepository``` et la classe ```ArticleJpa```.  
J'ai repris la même structure que pour le Mongo mais j'avais beaucoup d'erreurs que je ne comprennais pas, en relancant le lendemain, plus aucune erreur ne s'affichait.

## Suite du projet le 17/03 - JPA
### Les articles
Etant donné que l'énoncé nous parle d'articles, je créer dans ```core-domain``` la même structure que pour User mais cette fois ci pour les articles ```Article.java```, ```ArticleService.java``` et ```IDAOArticle.java```.

#### Structure finale du projet
```
projet_java
├── core-domain    
├── adapter-jpa    
├── adapter-mongo  
└── app       
```


### Contenu de core-domain

- **`Article.java`** : classe article avec les champs `id`, `titre` et `contenu`
- **`IDAOArticle.java`** : interface qui défini les opérations (`findAll`, `findById`, `save`, `deleteById`)
- **`ArticleService.java`** : service qui utilise `IDAOArticle`


### Contenu de adapter-jpa

- **`ArticleJpa.java`** : entité JPA avec la table `articles` en base (h2)
- **`DAOArticleJpa.java`** : implémente `IDAOArticle` et le lie à `ArticleJpa`


### Contenu de app

- **`Application.java`** : couvre `com.example.app` et `com.example.jpa` et `com.example.mongo`
- **`AppConfig.java`** : déclaration du bean `ArticleService` manuellement pointant vers `com.example.jpa` sinon ne marche pas
- **`ArticleController.java`** : exposition des opérations (`findAll`, `findById`, `save`, `deleteById`) sur `/articles`


### Problèmes rencontrés et solutions

| Problème | Solution |
|---|---|
| `Error resolving plugin` sur `adapter-jpa` | Supprimer les plugins dans `adapter-jpa/build.gradle` |
| `No qualifying bean of type 'ArticleJpaRepository'` | Ajouter `@EnableJpaRepositories` et `@EntityScan` dans `AppConfig.java` |
| `Port 8080 already in use` | Changer le port sur 8081 dans `application.yml` |


### Test sur Postman

Une fois l'application démarrée sur `http://localhost:8081`, on test l'API sur Postman :

| Méthode | URL | Description |
|---|---|---|
| `GET` | `/articles` | Récupère tous les articles |
| `GET` | `/articles/{id}` | Récupère un article par son ID |
| `POST` | `/articles` | Crée un nouvel article |
| `DELETE` | `/articles/{id}` | Supprime un article |


### Adaptation au code métier du TP

L'énoncé du TP impose un modèle précis, j'ai donc adapté les classes de la demo :

- **`Article.java`** : 
  - `id` généré automatiquement en **UUID** via `UUID.randomUUID().toString()` 
  - `titre` renommé en `title`
  - `contenu` renommé en `description`

- **`Response.java`** : 
  - `code` : code métier (le numéro pour les logs)
  - `message` : message qui décrit le résultat
  - `data` : données/résultat
  
- **`IDAOArticle.java`** : 
  - `findByTitle(String title)` : pour détecter les titres en double
  - `existsById(String id)` : pour vérifier l'existence (pour DELETE par exemple)


### ArticleService

`ArticleService` doit retourner les règles définies dans le TP :

| Méthode | Comportement |
|---|---|
| `getAll()` | Retourne `2002` avec la liste |
| `getById(id)` | `2002` trouvé, `7001` inexistant |
| `delete(id)` | `2002` et `true` supprimé, `7001` et`false` inexistant |
| `save(article)` | `2002` création, `2003` mise à jour, `7006` titre déjà utilisé |


### Test sur Postman

| Méthode | URL | yaml | Réponse attendue |
|---|---|---|---|
| `GET` | `/articles` | rien | `2002` liste |
| `GET` | `/articles/{id}` | rien | `2002` article ou `7001` |
| `POST` | `/articles` | `{"title": "...", "description": "..."}` | `2002` créé, `2003` mis à jour, `7006` titre mauvais |
| `DELETE` | `/articles/{id}` | rien | `2002` et `true` ou `7001` et `false` |


### adapter-mongo

Création de `adapter-mongo` sur le même principe que `adapter-jpa` :

- **`ArticleMongo.java`** : avec un `@Id`
- **`ArticleMongoRepository.java`** : avec `findByTitle()` pour vérifier les titres
- **`DAOArticleMongo.java`** : idem que `DAOArticleJpa` mais vers MongoDB


#### Création de la base MongoDB
On commence par la création de la DB sur MongoDB :  
<img width="808" height="650" alt="image" src="https://github.com/user-attachments/assets/e6f2b45e-9a1a-4131-8e0b-9a5078ccc442" />  


#### Basculer entre JPA et MongoDB

Pour choisir quelle implémentation, utiliser :

**`@Primary`**
Ajouter `@Primary` sur la DAO que l'on souhaite utiliser et le supprimer de celle que l'on ne souhaite pas utiliser.

Voici le résultat :  
<img width="1081" height="891" alt="image" src="https://github.com/user-attachments/assets/3c50f666-fab2-4e2a-bbea-1395a8a33aaf" />  
<img width="1068" height="460" alt="image" src="https://github.com/user-attachments/assets/58125061-9b98-413e-af7f-9066f74e5ad3" />  


#### Problèmes

| Problème | Solution |
|---|---|
| MongoDB ne fonctionnait pas malgré `@Primary` | Remplacer `IDAOArticle` par `DAOArticleMongo` dans `AppConfig` |
| `Bean of type 'DAOArticleMongo' could not be found` | Ajouter `"com.example.mongo"` dans `@SpringBootApplication` |
