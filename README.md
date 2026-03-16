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
