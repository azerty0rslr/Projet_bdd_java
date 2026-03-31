package com.example.domain;

import java.util.UUID;

// Classe représentant un article, ne dépend d'aucun framework
public class Article {

    private String id;
    private String title;
    private String description;

    // Génère un UUID comme identifiant
    public Article() {
        this.id = UUID.randomUUID().toString();
    }

    // Pour la reconstruction depuis la base de données
    public Article(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    // Pour la méthode GET
    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }

    // Pour la méthode POST
    public void setId(String id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}