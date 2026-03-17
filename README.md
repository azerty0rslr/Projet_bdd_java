# Projet BDD x JAVA
## Partie autonomie du 16/03
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

## Suite du projet le 17/03
### Les articles
Etant donné que l'énoncé nous parle d'articles, je créer dans ```core-domain``` la même structure que pour User mais cette fois ci pour les articles ```Article.java```, ```ArticleService.java``` et ```IDAOArticle.java```.  
