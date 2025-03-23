package org.articlesblog.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.articlesblog.dto.CreateArticleDTO;
import org.articlesblog.dto.EditArticleDTO;
import org.articlesblog.entity.Article;
import org.articlesblog.exception.ArticleHttpParametrizedException;
import org.articlesblog.mapper.ArticleMapper;
import org.articlesblog.repository.ArticleRepository;
import org.articlesblog.services.ArticleService;
import org.articlesblog.services.FirebaseStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final FirebaseStorageService firebaseStorageService;

    @Override
    @Transactional
    public EditArticleDTO createArticle(CreateArticleDTO articleDTO) {
        return articleMapper.toEditArticleDTO(
                articleRepository.save(
                        articleMapper.toArticle(articleDTO,
                                firebaseStorageService.uploadImage(articleDTO.getMultipartFile()),
                                ZonedDateTime.now())
                )
        );
    }

    @Override
    @Transactional
    public EditArticleDTO editArticle(UUID id, CreateArticleDTO articleDTO) {
        Article article = articleRepository.findById(id)
                .map(existingArticle -> {
                    String imageURL = articleDTO.getMultipartFile().isEmpty() ?
                            existingArticle.getImage() :
                            firebaseStorageService.updateImage(
                                    existingArticle.getImage(),
                                    articleDTO.getMultipartFile()
                            );

                    return articleRepository.save(
                            articleMapper.toArticle(articleDTO, imageURL, ZonedDateTime.now())
                    );
                })
                .orElseThrow(() -> new ArticleHttpParametrizedException(
                        HttpStatus.NOT_FOUND,
                        "Article not found",
                        "No Article entity was found by id: " + id));

        return articleMapper.toEditArticleDTO(article);
    }

    @Override
    @Transactional
    public String deleteArticle(UUID id) {
        Optional<Article> articleOptional = articleRepository.findById(id);
        return articleOptional.map(article -> {
            articleRepository.deleteById(id);
            try {
                firebaseStorageService.deleteImage(article.getImage());
            } catch (NullPointerException e) {
                return "Image has been deleted";
            }
            return "Article " + id + " deleted";
        }).orElseThrow(() -> new ArticleHttpParametrizedException(
                HttpStatus.NOT_FOUND,
                "Article not found",
                "No Article entity was found by id: " + id));
    }
}