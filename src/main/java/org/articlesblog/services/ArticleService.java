package org.articlesblog.services;

import org.articlesblog.dto.CreateArticleDTO;
import org.articlesblog.dto.EditArticleDTO;
import org.articlesblog.dto.GetAllArticlesDTO;
import org.articlesblog.dto.GetArticleDTO;

import java.util.List;

public interface ArticleService {
    GetArticleDTO getArticle(Integer id);
    EditArticleDTO getToEditArticle(Integer id);
    List<GetAllArticlesDTO> getAllArticles();
    EditArticleDTO createArticle(CreateArticleDTO articleDTO);
    EditArticleDTO editArticle(Integer id, CreateArticleDTO articleDTO);
    String deleteArticle(Integer id);
    String markdownToHTML(String markdown);
}
