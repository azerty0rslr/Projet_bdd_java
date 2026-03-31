package com.example.domain;

import java.util.List;
import java.util.Optional;

// Pour les opérations de persistance pour les articles (tests)
public interface IDAOArticle {
    List<Article> findAll();
    Optional<Article> findById(String id);

    // Détecter double titre
    Optional<Article> findByTitle(String title);
    Article save(Article article);
    boolean deleteById(String id);

    // Vérifier l'existence avant suppression ou MAJ
    boolean existsById(String id);
}