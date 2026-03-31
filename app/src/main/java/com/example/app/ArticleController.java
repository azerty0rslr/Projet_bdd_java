package com.example.app;

import com.example.domain.Article;
import com.example.domain.ArticleService;
import com.example.domain.Response;
import org.springframework.web.bind.annotation.*;

@RestController
//
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    // Méthode GET de tout
    @GetMapping
    public Response getAll() {
        return articleService.getAll();
    }

    // Méthode GET par id
    @GetMapping("/{id}")
    public Response getById(@PathVariable String id) {
        return articleService.getById(id);
    }

    // Méthode POST pour publier un article
    @PostMapping
    public Response save(@RequestBody Article article) {
        return articleService.save(article);
    }

    // Méthode DELETE pour supprimer un article par son ID
    @DeleteMapping("/{id}")
    public Response delete(@PathVariable String id) {
        return articleService.delete(id);
    }
}