package com.example.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleJpaRepository extends JpaRepository<ArticleJpa, String> {
    // Obliger de rajouter le FindBy sinon ne fonctionne pas
    Optional<ArticleJpa> findByTitle(String title);
}