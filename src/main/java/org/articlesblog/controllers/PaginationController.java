package org.articlesblog.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.articlesblog.dto.GetAllArticlesDTO;
import org.articlesblog.services.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Tag(name = "Pagination")
@Slf4j
@RequiredArgsConstructor
@Controller
public class PaginationController {
    private final ArticleService articleService;

    @GetMapping("/articles/page/{id}")
    @Operation(summary = "Получение статей по страницам")
    public String getArticlesByPage(@PathVariable Integer id, Model model) {
        List<GetAllArticlesDTO> articles = articleService.getAllArticles();

        model.addAttribute("searched", false);
        log.info("Выводим страницу {}: ", id);
        return getPages(id, model, articles);
    }

    private String getPages(Integer id, Model model, List<GetAllArticlesDTO> articles) {
        int pageSize = 9;
        int totalPages = (int) Math.ceil((double) articles.size() / pageSize);
        int startIndex = (id - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, articles.size());

        if (articles.isEmpty()) {
            return "articles";
        } else if (id > totalPages) {
            model.addAttribute("errorMes", "-Максимальная страница: " + totalPages);
            return "error";
        } else if (id < 1) {
            model.addAttribute("errorMes", "-Минимальная страница: 1");
            return "error";
        }

        List<GetAllArticlesDTO> articlesOnPage = new ArrayList<>();

        for (int i = endIndex - 1; i >= startIndex; i--) {
            articlesOnPage.add(articles.get(i));
        }

        for (GetAllArticlesDTO article : articlesOnPage) {
            log.info("{}", article);
        }

        model.addAttribute("articles", articlesOnPage);
        model.addAttribute("title", "Страница " + id + " из " + totalPages);
        model.addAttribute("currentPage", id);
        model.addAttribute("totalPages", totalPages);
        return "articles";
    }
}
