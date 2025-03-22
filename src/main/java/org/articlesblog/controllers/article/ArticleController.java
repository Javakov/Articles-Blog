package org.articlesblog.controllers.article;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.articlesblog.dto.CreateArticleDTO;
import org.articlesblog.dto.EditArticleDTO;
import org.articlesblog.services.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Article's blog")
@Slf4j
@RequiredArgsConstructor
@RestController
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping("/articles/new")
    @Operation(summary = "Создание новой статьи")
    public ResponseEntity<EditArticleDTO> createArticle(@ModelAttribute CreateArticleDTO articleDTO) {
        EditArticleDTO createdArticle = articleService.createArticle(articleDTO);
        log.info("Созданная статья: " + createdArticle);
        return ResponseEntity.ok(createdArticle);
    }

    @PostMapping("/articles/edit/{id}")
    @Operation(summary = "Обновление данных статьи по id")
    public ResponseEntity<EditArticleDTO> updateArticle(@PathVariable Integer id, @ModelAttribute CreateArticleDTO articleDTO) {
        EditArticleDTO updatedArticle = articleService.editArticle(id, articleDTO);
        log.info("Обновленная статья: " + updatedArticle);
        return ResponseEntity.ok(updatedArticle);
    }

    @PostMapping("/articles/delete/{id}")
    @Operation(summary = "Удаление статьи по id")
    public ResponseEntity<String> deleteArticle(@PathVariable Integer id) {
        String deletedArticle = articleService.deleteArticle(id);
        log.info(deletedArticle);
        return ResponseEntity.ok(deletedArticle);
    }
}
