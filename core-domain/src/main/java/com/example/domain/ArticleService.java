package com.example.domain;

import java.util.List;

// Service métier gérant la logique des articles
// Dépend uniquement de l'interface IDAOArticle, jamais d'une implémentation concrète
// Spring injectera automatiquement la bonne implémentation (JPA ou MongoDB)
public class ArticleService {

    private final IDAOArticle idaoArticle;

    public ArticleService(IDAOArticle idaoArticle) {
        this.idaoArticle = idaoArticle;
    }

    // Récupère tous les articles
    public Response getAll() {
        List<Article> articles = idaoArticle.findAll();
        return new Response(2002, "La liste des articles a été récupérée avec succès", articles);
    }

    // Récupère un article par id, retourne une erreur si n'existe pas
    public Response getById(String id) {
        return idaoArticle.findById(id)
                .map(a -> new Response(2002, "L'article a été récupéré avec succès", a))
                .orElse(new Response(7001, "L'article n'existe pas", null));
    }

    // Supprime un article par son id, retourne une erreur si l'article n'existe pas avant suppression
    public Response delete(String id) {
        if (!idaoArticle.existsById(id)) {
            return new Response(7001, "L'article n'existe pas", false);
        }
        idaoArticle.deleteById(id);
        return new Response(2002, "L'article a été supprimé avec succès", true);
    }

    // Sauvegarde un article (création ou maj d'article)
    public Response save(Article article) {
        // On vérifie la persistance du titre (voir IDAO)
        boolean titreExistant = idaoArticle.findByTitle(article.getTitle())
                .isPresent();

        // Tester le titre
        if (titreExistant) {
            return new Response(7006, "Le titre est déjà utilisé", null);
        }

        // Si l'id existe déjà -> maj
        if (article.getId() != null && idaoArticle.existsById(article.getId())) {
            Article updated = idaoArticle.save(article);
            return new Response(2003, "Article modifié avec succès", updated);
        }

        // Sinon -> création
        Article created = idaoArticle.save(article);
        return new Response(2002, "Article créé avec succès", created);
    }
}