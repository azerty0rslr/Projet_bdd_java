package com.example.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ArticleMongoRepository extends MongoRepository<ArticleMongo, String> {
    // Pareil que pour JPA ne fonctionne pas sans
    Optional<ArticleMongo> findByTitle(String title);
}