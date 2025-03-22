package org.articlesblog.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.articlesblog.dto.CreateArticleDTO;
import org.articlesblog.dto.EditArticleDTO;
import org.articlesblog.dto.GetAllArticlesDTO;
import org.articlesblog.dto.GetArticleDTO;
import org.articlesblog.entity.Article;
import org.articlesblog.repository.ArticleRepository;
import org.articlesblog.services.ArticleService;
import org.articlesblog.services.FirebaseStorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final FirebaseStorageService firebaseStorageService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm");

    @Override
    public GetArticleDTO getArticle(Integer id) {
        return articleRepository.findById(id)
                .map(article -> {
                    String createDate = article.getDateCreate().format(formatter);
                    String changeDate = article.getDateChange() != null ? article.getDateChange().format(formatter) : "-";

                    return new GetArticleDTO(article.getId(), article.getTitle(), markdownToHTML(article.getText()),
                            article.getAuthor(),  article.getLabel(), createDate, changeDate, article.getImage());
                })
                .orElse(null);
    }

    @Override
    public EditArticleDTO getToEditArticle(Integer id) {
        return articleRepository.findById(id)
                .map(article -> new EditArticleDTO(article.getId(), article.getTitle(), article.getDescription(), article.getText(),
                        article.getAuthor(),  article.getLabel(), article.getImage()))
                .orElse(null);
    }

    @Override
    public List<GetAllArticlesDTO> getAllArticles() {
        List<Article> articles = articleRepository.findAllSortedById();
        List<GetAllArticlesDTO> articleDTOs = new ArrayList<>();
        for (Article article : articles) {
            String createDate = article.getDateCreate().format(formatter);
            GetAllArticlesDTO articleDTO = new GetAllArticlesDTO(
                    article.getId(),
                    article.getLabel(),
                    article.getAuthor(),
                    article.getTitle(),
                    article.getDescription(),
                    createDate,
                    article.getImage()
            );
            articleDTOs.add(0, articleDTO);
        }
        return articleDTOs;
    }

    @Override
    @Transactional
    public EditArticleDTO createArticle(CreateArticleDTO articleDTO) {
        Article article = new Article();
        article.setTitle(articleDTO.getTitle());
        article.setDescription(articleDTO.getDescription());
        article.setAuthor(articleDTO.getAuthor());
        article.setLabel(articleDTO.getLabel());
        article.setText(articleDTO.getText());
        article.setImage(firebaseStorageService.uploadImage(articleDTO.getMultipartFile()));
        article.setDateCreate(LocalDateTime.now());
        article.setDateChange(LocalDateTime.now());

        Article savedArticle = articleRepository.save(article);

        return new EditArticleDTO(savedArticle.getId(), savedArticle.getTitle(), savedArticle.getDescription(), savedArticle.getText(),
                savedArticle.getAuthor(), savedArticle.getLabel(), savedArticle.getImage());
    }

    @Override
    @Transactional
    public EditArticleDTO editArticle(Integer id, CreateArticleDTO articleDTO) {
        Article article = articleRepository.findById(id)
                .map(existingArticle -> {
                    String imageURL = existingArticle.getImage();

                    if (!articleDTO.getMultipartFile().isEmpty()) {
                        imageURL = firebaseStorageService.updateImage(existingArticle.getImage(), articleDTO.getMultipartFile());
                    }

                    existingArticle.setTitle(articleDTO.getTitle());
                    existingArticle.setDescription(articleDTO.getDescription());
                    existingArticle.setAuthor(articleDTO.getAuthor());
                    existingArticle.setLabel(articleDTO.getLabel());
                    existingArticle.setText(articleDTO.getText());
                    existingArticle.setImage(imageURL);
                    existingArticle.setDateChange(LocalDateTime.now());

                    return articleRepository.save(existingArticle);
                })
                .orElseThrow(() -> new RuntimeException("Статья с id " + id + " не найдена."));

        return new EditArticleDTO(article.getId(), article.getTitle(), article.getDescription(), article.getText(),
                article.getAuthor(), article.getLabel(), article.getImage());
    }

    @Override
    @Transactional
    public String deleteArticle(Integer id) {
        Optional<Article> articleOptional = articleRepository.findById(id);
        return articleOptional.map(article -> {
            articleRepository.deleteById(id);
            try {
                firebaseStorageService.deleteImage(article.getImage());
            }
            catch (NullPointerException e){
                return "Картинка удалена";
            }
            return "Статья " + id + " удалена";
        }).orElse("Статья не найдена");
    }

    @Override
    public String markdownToHTML(String markdown) {
        Parser parser = Parser.builder()
                .build();

        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder()
                .build();
        log.info("Преобразуем markdownToHTML");
        return renderer.render(document);
    }
}