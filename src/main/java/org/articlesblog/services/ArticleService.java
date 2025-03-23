package org.articlesblog.services;

import org.articlesblog.dto.CreateArticleDTO;
import org.articlesblog.dto.EditArticleDTO;

import java.util.UUID;

public interface ArticleService {
    EditArticleDTO createArticle(CreateArticleDTO articleDTO);

    EditArticleDTO editArticle(UUID id, CreateArticleDTO articleDTO);

    String deleteArticle(UUID id);
}
