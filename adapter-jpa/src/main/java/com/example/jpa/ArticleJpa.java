package com.example.jpa;

import jakarta.persistence.*;

@Entity
@Table(name = "articles")
public class ArticleJpa {

    @Id
    private String id;

    private String title;
    private String description;

    public ArticleJpa() {}

    // article
    public ArticleJpa(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    // Pour le GET
    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }

    // Pour le POST
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