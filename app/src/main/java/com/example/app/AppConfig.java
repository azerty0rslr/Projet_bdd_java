package com.example.app;

import com.example.domain.ArticleService;
import com.example.mongo.DAOArticleMongo;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
// Indique à Spring où chercher les repositories JPA
@EnableJpaRepositories(basePackages = "com.example.jpa")
// Indique à Spring où chercher les repositories MongoDB
@EnableMongoRepositories(basePackages = "com.example.mongo")
// Indique à Hibernate où chercher les entités @Entity
@EntityScan(basePackages = "com.example.jpa")
public class AppConfig {

    // On crée le bean ArticleService manuellement car il est dans core-domain
    // et ne doit pas avoir d'annotations Spring
    // On injecte explicitement DAOArticleMongo pour utiliser MongoDB
    // Pour basculer sur JPA, remplacer DAOArticleMongo par DAOArticleJpa
    @Bean
    public ArticleService articleService(DAOArticleMongo idaoArticle) {
        return new ArticleService(idaoArticle);
    }
}