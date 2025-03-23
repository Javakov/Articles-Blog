package org.articlesblog.controllers.impl;

import lombok.RequiredArgsConstructor;
import org.articlesblog.controllers.ArticleController;
import org.articlesblog.dto.CreateArticleDTO;
import org.articlesblog.dto.EditArticleDTO;
import org.articlesblog.services.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ArticleControllerImpl implements ArticleController {
    private final ArticleService articleService;

    @Override
    public ResponseEntity<EditArticleDTO> createArticle(@ModelAttribute CreateArticleDTO articleDTO) {
        EditArticleDTO createdArticle = articleService.createArticle(articleDTO);
        return ResponseEntity.ok(createdArticle);
    }

    @Override
    public ResponseEntity<EditArticleDTO> updateArticle(
            @PathVariable UUID id, @ModelAttribute CreateArticleDTO articleDTO
    ) {
        EditArticleDTO updatedArticle = articleService.editArticle(id, articleDTO);
        return ResponseEntity.ok(updatedArticle);
    }

    @Override
    public ResponseEntity<String> deleteArticle(@PathVariable UUID id) {
        String deletedArticle = articleService.deleteArticle(id);
        return ResponseEntity.ok(deletedArticle);
    }
}
