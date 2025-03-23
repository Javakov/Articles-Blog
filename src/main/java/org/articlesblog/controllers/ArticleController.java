package org.articlesblog.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.articlesblog.dto.CreateArticleDTO;
import org.articlesblog.dto.EditArticleDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@Tag(name = "Article's blog")
public interface ArticleController {

    @PostMapping("/articles/new")
    @Operation(summary = "Создание новой статьи")
    ResponseEntity<EditArticleDTO> createArticle(@ModelAttribute CreateArticleDTO articleDTO);

    @PostMapping("/articles/edit/{id}")
    @Operation(summary = "Обновление данных статьи по id")
    ResponseEntity<EditArticleDTO> updateArticle(
            @PathVariable UUID id, @ModelAttribute CreateArticleDTO articleDTO);

    @PostMapping("/articles/delete/{id}")
    @Operation(summary = "Удаление статьи по id")
    ResponseEntity<String> deleteArticle(@PathVariable UUID id);
}
