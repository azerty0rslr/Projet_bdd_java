package com.example.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "articles")
public class ArticleMongo {

    @Id
    private String id;
    private String title;
    private String description;

    public ArticleMongo() {}

    // Pour l'article Mongo
    public ArticleMongo(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    // Pour trouver l'article Mongo
    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }

    // Pour créer un article Mongo
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