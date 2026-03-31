package com.example.mongo;

import com.example.domain.Article;
import com.example.domain.IDAOArticle;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Supprimer le Primary pour utiliser le JPA
@Primary
@Repository
public class DAOArticleMongo implements IDAOArticle {

    private final ArticleMongoRepository repository;

    public DAOArticleMongo(ArticleMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Article> findAll() {
        // On récupère tous les articles
        return repository.findAll()
                .stream()
                .map(this::toArticle)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Article> findById(String id) {
        // On récupère l'article par son ID
        return repository.findById(id)
                .map(this::toArticle);
    }

    @Override
    public Optional<Article> findByTitle(String title) {
        // On récupère l'article par son titre
        return repository.findByTitle(title)
                .map(this::toArticle);
    }

    @Override
    public Article save(Article article) {
        // Pour ajouter un nouvel article avec POST
        // Si erreur pour vérifier que MongoDB est bien utilisé
        System.out.println(">>> MONGO save() appelé");
        ArticleMongo mongo = toArticleMongo(article);
        ArticleMongo saved = repository.save(mongo);
        return toArticle(saved);
    }

    @Override
    public boolean deleteById(String id) {
        // Pour supprimer un article
        repository.deleteById(id);
        return true;
    }

    @Override
    public boolean existsById(String id) {
        // vérifier que l'article existe
        return repository.existsById(id);
    }

    // Convertit un ArticleMongo en Article
    private Article toArticle(ArticleMongo mongo) {
        return new Article(mongo.getId(), mongo.getTitle(), mongo.getDescription());
    }

    // Convertit un Article en ArticleMongo
    private ArticleMongo toArticleMongo(Article article) {
        return new ArticleMongo(article.getId(), article.getTitle(), article.getDescription());
    }
}