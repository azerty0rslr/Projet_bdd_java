package com.example.mongo;

import com.example.domain.Article;
import com.example.domain.IDAOArticle;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Primary
@Repository
public class DAOArticleMongo implements IDAOArticle {

    private final ArticleMongoRepository repository;

    public DAOArticleMongo(ArticleMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Article> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toArticle)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Article> findById(String id) {
        return repository.findById(id)
                .map(this::toArticle);
    }

    @Override
    public Optional<Article> findByTitle(String title) {
        return repository.findByTitle(title)
                .map(this::toArticle);
    }

    @Override
    public Article save(Article article) {
        System.out.println(">>> MONGO save() appelé"); // vérifier que MongoDB est bien utilisé
        ArticleMongo mongo = toArticleMongo(article);
        ArticleMongo saved = repository.save(mongo);
        return toArticle(saved);
    }

    @Override
    public boolean deleteById(String id) {
        repository.deleteById(id);
        return true;
    }

    @Override
    public boolean existsById(String id) {
        return repository.existsById(id);
    }

    // --- Mappers ---

    private Article toArticle(ArticleMongo mongo) {
        return new Article(mongo.getId(), mongo.getTitle(), mongo.getDescription());
    }

    private ArticleMongo toArticleMongo(Article article) {
        return new ArticleMongo(article.getId(), article.getTitle(), article.getDescription());
    }
}