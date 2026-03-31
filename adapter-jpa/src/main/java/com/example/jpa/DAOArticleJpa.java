package com.example.jpa;

import com.example.domain.Article;
import com.example.domain.IDAOArticle;
// Si on rajoute @Primary pour l'utiliser on rajoute aussi : import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Implémente JPA de IDAOArticle
// Rajouter @Primary si on utilise le JPA et non pas Mongo
@Repository
public class DAOArticleJpa implements IDAOArticle {

    private final ArticleJpaRepository repository;

    public DAOArticleJpa(ArticleJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Article> findAll() {
        // On récupère tous les ArticleJpa et on les convertit en Article du domaine
        return repository.findAll()
                .stream()
                .map(this::toArticle)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Article> findById(String id) {
        // On récupère l'article par son ID et on le convertit en Article du domaine
        return repository.findById(id)
                .map(this::toArticle);
    }

    @Override
    public Optional<Article> findByTitle(String title) {
        // On récupère l'article par son titre et on le convertit en Article du domaine
        return repository.findByTitle(title)
                .map(this::toArticle);
    }

    @Override
    public Article save(Article article) {
        // On crée l'article avec la méthode POST
        ArticleJpa jpa = toArticleJpa(article);
        ArticleJpa saved = repository.save(jpa);
        return toArticle(saved);
    }

    @Override
    public boolean deleteById(String id) {
        // On supprime l'article avec DELETE
        repository.deleteById(id);
        return true;
    }

    @Override
    public boolean existsById(String id) {
        // Pour vérifier si l'id existe
        return repository.existsById(id);
    }

    // Convertit un ArticleJpa en Article
    private Article toArticle(ArticleJpa jpa) {

        return new Article(jpa.getId(), jpa.getTitle(), jpa.getDescription());
    }

    // Convertit un Article en ArticleJpa
    private ArticleJpa toArticleJpa(Article article) {
        return new ArticleJpa(article.getId(), article.getTitle(), article.getDescription());
    }
}