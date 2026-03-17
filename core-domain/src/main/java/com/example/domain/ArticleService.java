package com.example.domain;

import java.util.List;

// Service métier gérant la logique des articles
// Dépend uniquement de l'interface IDAOArticle, jamais d'une implémentation concrète
// Spring injectera automatiquement la bonne implémentation (JPA ou MongoDB)
public class ArticleService {

    private final IDAOArticle idaoArticle;

    // Injection par constructeur : pas d'annotation Spring car ce service
    // appartient au domaine métier pur
    public ArticleService(IDAOArticle idaoArticle) {
        this.idaoArticle = idaoArticle;
    }

    // Récupère tous les articles
    public Response getAll() {
        List<Article> articles = idaoArticle.findAll();
        return new Response(2002, "La liste des articles a été récupérée avec succès", articles);
    }

    // Récupère un article par son id, retourne une erreur 7001 si inexistant
    public Response getById(String id) {
        return idaoArticle.findById(id)
                .map(a -> new Response(2002, "L'article a été récupéré avec succès", a))
                .orElse(new Response(7001, "L'article n'existe pas", null));
    }

    // Supprime un article par son id
    // Retourne 7001 si l'article n'existe pas avant même d'essayer de supprimer
    public Response delete(String id) {
        if (!idaoArticle.existsById(id)) {
            return new Response(7001, "L'article n'existe pas", false);
        }
        idaoArticle.deleteById(id);
        return new Response(2002, "L'article a été supprimé avec succès", true);
    }

    // Sauvegarde un article (création ou mise à jour)
    public Response save(Article article) {
        // On vérifie si un autre article utilise déjà ce titre
        // Le filter exclut l'article lui-même pour permettre une mise à jour sans changer le titre
        boolean titreExistant = idaoArticle.findByTitle(article.getTitle())
                .filter(a -> !a.getId().equals(article.getId()))
                .isPresent();

        if (titreExistant) {
            return new Response(7006, "Le titre est déjà utilisé", null);
        }

        // Si l'id existe déjà en base → mise à jour
        if (article.getId() != null && idaoArticle.existsById(article.getId())) {
            Article updated = idaoArticle.save(article);
            return new Response(2003, "Article modifié avec succès", updated);
        }

        // Sinon → création
        Article created = idaoArticle.save(article);
        return new Response(2002, "Article créé avec succès", created);
    }
}