package com.example.domain;

import java.util.List;
import java.util.Optional;

// Interface définissant les opérations de persistance pour les articles
// Le domaine ne connaît que cette interface, jamais son implémentation concrète (JPA ou MongoDB)
// C'est le principe d'inversion de dépendance (DIP)
public interface IDAOArticle {
    List<Article> findAll();
    Optional<Article> findById(String id);
    // Utilisée pour détecter les doublons de titre
    Optional<Article> findByTitle(String title);
    Article save(Article article);
    boolean deleteById(String id);
    // Utilisée pour vérifier l'existence avant suppression ou mise à jour
    boolean existsById(String id);
}