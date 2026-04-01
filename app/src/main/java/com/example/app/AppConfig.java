package com.example.app;

// Problème de fonctionnement sans AppConfig, une fois le adapter-mongo créé
import com.example.domain.ArticleService;
import com.example.mongo.DAOArticleMongo;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
// Problème de No qualifying bean of type 'ArticleJpaRepository', obligé de le rajouter pour que fonctionne
@EnableJpaRepositories(basePackages = "com.example.jpa")
// Problème de No qualifying bean of type 'ArticleJpaRepository', obligé de le rajouter pour que fonctionne
@EnableMongoRepositories(basePackages = "com.example.mongo")
// Problème de No qualifying bean of type 'ArticleJpaRepository', obligé de le rajouter pour que fonctionne
@EntityScan(basePackages = "com.example.jpa")
public class AppConfig {

    // Pour passer sur JPA remplacer DAOArticleMongo par DAOArticleJpa
    @Bean
    public ArticleService articleService(DAOArticleMongo idaoArticle) {
        return new ArticleService(idaoArticle);
    }
}