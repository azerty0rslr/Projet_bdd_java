package com.example.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ArticleMongoRepository extends MongoRepository<ArticleMongo, String> {
    Optional<ArticleMongo> findByTitle(String title);
}