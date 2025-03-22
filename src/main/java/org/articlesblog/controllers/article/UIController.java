package org.articlesblog.controllers.article;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.articlesblog.dto.EditArticleDTO;
import org.articlesblog.dto.GetArticleDTO;
import org.articlesblog.services.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Article's blog")
@Slf4j
@RequiredArgsConstructor
@Controller
public class UIController {
    private final ArticleService articleService;

    @GetMapping("/articles/{id}")
    @Operation(summary = "Получение данных статьи по id")
    public String getArticle(@PathVariable Integer id, Model model) {
        try {
            GetArticleDTO article = articleService.getArticle(id);
            model.addAttribute("article", article);
            model.addAttribute("title", article.getTitle());
            log.info("Полученная статья: " + article);
            return "article/get-article";
        } catch (Exception e) {
            model.addAttribute("errorMes", "-Страницы " + id + " не существует.");
            return "error";
        }
    }

    @GetMapping("/articles/new")
    @Operation(summary = "Получение страницы создания статьи")
    public String createArticle(Model model) {
        try {
            log.info("Открываем страницу создания...");
            return "article/create-article";
        } catch (Exception e) {
            model.addAttribute("errorMes", "-Ошибка при получении страницы создания статьи.");
            return "error";
        }
    }

    @GetMapping("/articles/edit/{id}")
    @Operation(summary = "Получение страницы редактирования статьи по id")
    public String updateArticle(@PathVariable Integer id, Model model) {
        try {
            EditArticleDTO article = articleService.getToEditArticle(id);
            model.addAttribute("article", article);
            model.addAttribute("title", article.getTitle());
            log.info("Получение версии статьи до редактирования...");
            return "article/edit-article";
        } catch (Exception e) {
            model.addAttribute("errorMes", "-Страницы " + id + " не существует.");
            return "error";
        }
    }
}
