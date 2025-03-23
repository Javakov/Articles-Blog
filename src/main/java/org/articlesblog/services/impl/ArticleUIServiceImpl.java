package org.articlesblog.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.articlesblog.dto.EditArticleDTO;
import org.articlesblog.dto.GetAllArticlesDTO;
import org.articlesblog.dto.GetArticleDTO;
import org.articlesblog.entity.Article;
import org.articlesblog.exception.ArticleHttpParametrizedException;
import org.articlesblog.mapper.ArticleMapper;
import org.articlesblog.repository.ArticleRepository;
import org.articlesblog.services.ArticleUIService;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArticleUIServiceImpl implements ArticleUIService {
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm");

    @Override
    public GetArticleDTO getArticle(UUID id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ArticleHttpParametrizedException(
                        HttpStatus.NOT_FOUND,
                        "Article not found",
                        "No Article entity was found by id: " + id));

        String createDate = article.getDateCreate().format(formatter);
        String changeDate = article.getDateChange() != null ?
                article.getDateChange().format(formatter) : "-";
        String text = markdownToHTML(article.getText());

        return articleMapper.toGetArticleDTO(article, text, createDate, changeDate);
    }

    @Override
    public EditArticleDTO getToEditArticle(UUID id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ArticleHttpParametrizedException(
                        HttpStatus.NOT_FOUND,
                        "Article not found",
                        "No Article entity was found by id: " + id));

        return articleMapper.toEditArticleDTO(article);
    }

    @Override
    public Page<GetAllArticlesDTO> getAllArticles(int page) {
        return getArticlesPage(page, articleRepository::findAll);
    }

    @Override
    public Page<GetAllArticlesDTO> searchArticles(String searchText, int page) {
        return getArticlesPage(page, (p) -> articleRepository.searchByText(searchText, p));
    }

    private Page<GetAllArticlesDTO> getArticlesPage(int page, Function<Pageable, Page<Article>> findMethod) {
        Pageable pageable = PageRequest.of(
                page - 1,
                9,
                Sort.by("dateCreate").descending()
        );

        Page<Article> articles = findMethod.apply(pageable);

        return articles.map(article -> articleMapper.toGetAllArticlesDTO(
                article,
                article.getDateCreate().format(formatter)
        ));
    }

    public String markdownToHTML(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();

        return renderer.render(document);
    }
}