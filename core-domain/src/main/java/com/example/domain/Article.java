package com.example.domain;

import java.util.UUID;

// Classe métier représentant un article
// Elle ne dépend d'aucun framework (pas de Spring, pas de JPA)
public class Article {

    private String id;
    private String title;
    private String description;

    // Constructeur par défaut : génère automatiquement un UUID unique comme identifiant
    public Article() {
        this.id = UUID.randomUUID().toString();
    }

    // Constructeur utilisé lors de la reconstruction depuis la base de données
    public Article(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public String getId()           { return id; }
    public String getTitle()        { return title; }
    public String getDescription()  { return description; }

    public void setId(String id)                        { this.id = id; }
    public void setTitle(String title)                  { this.title = title; }
    public void setDescription(String description)      { this.description = description; }
}