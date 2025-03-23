package org.articlesblog.services;

import org.articlesblog.dto.EditArticleDTO;
import org.articlesblog.dto.GetAllArticlesDTO;
import org.articlesblog.dto.GetArticleDTO;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface ArticleUIService {
    GetArticleDTO getArticle(UUID id);

    EditArticleDTO getToEditArticle(UUID id);

    Page<GetAllArticlesDTO> getAllArticles(int page);

    Page<GetAllArticlesDTO> searchArticles(String searchText, int page);
}
