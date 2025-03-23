package org.articlesblog.controllers.impl;

import lombok.RequiredArgsConstructor;
import org.articlesblog.controllers.ArticleUIController;
import org.articlesblog.dto.EditArticleDTO;
import org.articlesblog.dto.GetAllArticlesDTO;
import org.articlesblog.dto.GetArticleDTO;
import org.articlesblog.services.ArticleUIService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ArticleUIControllerImpl implements ArticleUIController {
    private final ArticleUIService articleUIService;

    @Override
    public String getArticle(@PathVariable UUID id, Model model) {
        try {
            GetArticleDTO article = articleUIService.getArticle(id);
            model.addAttribute("article", article);
            model.addAttribute("title", article.getTitle());
            return "article/get-article";
        } catch (Exception e) {
            model.addAttribute("errorMes", "Страницы " + id + " не существует.");
            return "error";
        }
    }

    @Override
    public String createArticle(Model model) {
        try {
            return "article/create-article";
        } catch (Exception e) {
            model.addAttribute("errorMes", "-Ошибка при получении страницы создания статьи.");
            return "error";
        }
    }

    @Override
    public String updateArticle(@PathVariable UUID id, Model model) {
        try {
            EditArticleDTO article = articleUIService.getToEditArticle(id);
            model.addAttribute("article", article);
            model.addAttribute("title", article.getTitle());
            return "article/edit-article";
        } catch (Exception e) {
            model.addAttribute("errorMes", "-Страницы " + id + " не существует.");
            return "error";
        }
    }

    @Override
    public String getArticlesByPage(@PathVariable Integer id, Model model) {
        if (id < 1) {
            model.addAttribute("errorMes", "-Минимальная страница: 1");
            return "error";
        }

        Page<GetAllArticlesDTO> articlePage = articleUIService.getAllArticles(id);

        if (articlePage.isEmpty() && id > 1) {
            model.addAttribute("errorMes", "-Максимальная страница: " + articlePage.getTotalPages());
            return "error";
        }

        List<GetAllArticlesDTO> articlesOnPage = articlePage.getContent();

        model.addAttribute("articles", articlesOnPage);
        model.addAttribute("title", "Страница " + id + " из " + articlePage.getTotalPages());
        model.addAttribute("currentPage", id);
        model.addAttribute("totalPages", articlePage.getTotalPages());
        model.addAttribute("searched", false);

        return "articles";
    }

    @Override
    public String searchArticlesByPage(@PathVariable Integer id, @RequestParam String searchText, Model model) {
        if (id < 1) {
            model.addAttribute("errorMes", "-Минимальная страница: 1");
            return "error";
        }

        Page<GetAllArticlesDTO> articlePage = articleUIService.searchArticles(searchText, id);
        List<GetAllArticlesDTO> articlesOnPage = articlePage.getContent();

        model.addAttribute("articles", articlesOnPage);
        model.addAttribute("title", "Страница " + id + " из " + articlePage.getTotalPages());
        model.addAttribute("currentPage", id);
        model.addAttribute("totalPages", articlePage.getTotalPages());
        model.addAttribute("searched", true);
        model.addAttribute("searchText", searchText);

        return "articles";
    }

    @Override
    public String about(Model model) {
        model.addAttribute("title", "О нас");
        return "about";
    }
}
