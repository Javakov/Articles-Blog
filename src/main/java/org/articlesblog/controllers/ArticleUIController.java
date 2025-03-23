package org.articlesblog.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Tag(name = "Article's blog")
public interface ArticleUIController {

    @GetMapping("/articles/{id}")
    @Operation(summary = "Получение данных статьи по id")
    String getArticle(@PathVariable UUID id, Model model);

    @GetMapping("/articles/new")
    @Operation(summary = "Получение страницы создания статьи")
    String createArticle(Model model);

    @GetMapping("/articles/edit/{id}")
    @Operation(summary = "Получение страницы редактирования статьи по id")
    String updateArticle(@PathVariable UUID id, Model model);

    @GetMapping("/articles/page/{id}")
    @Operation(summary = "Получение статей по страницам")
    String getArticlesByPage(@PathVariable Integer id, Model model);

    @GetMapping("/articles/search/page/{id}")
    @Operation(summary = "Поиск статей по тексту")
    String searchArticlesByPage(@PathVariable Integer id, @RequestParam String searchText, Model model);

    @GetMapping("/about")
    @Operation(summary = "О нас")
    String about(Model model);
}
