package org.articlesblog.mapper;

import org.articlesblog.dto.CreateArticleDTO;
import org.articlesblog.dto.EditArticleDTO;
import org.articlesblog.dto.GetAllArticlesDTO;
import org.articlesblog.dto.GetArticleDTO;
import org.articlesblog.entity.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@Mapper(componentModel = "spring")
public interface ArticleMapper {
    EditArticleDTO toEditArticleDTO(Article article);

    @Mapping(target = "text", source = "text")
    @Mapping(target = "dateCreate", source = "dateCreate")
    @Mapping(target = "dateChange", source = "dateChange")
    GetArticleDTO toGetArticleDTO(Article article, String text, String dateCreate, String dateChange);

    @Mapping(target = "dateCreate", source = "dateCreate")
    GetAllArticlesDTO toGetAllArticlesDTO(Article article, String dateCreate);

    @Mapping(target = "image", source = "image")
    @Mapping(target = "dateCreate", source = "currentDate")
    @Mapping(target = "dateChange", source = "currentDate")
    Article toArticle(CreateArticleDTO editArticleDTO, String image, ZonedDateTime currentDate);

}
