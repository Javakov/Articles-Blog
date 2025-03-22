package org.articlesblog.controllers.gigachat;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.articlesblog.config.GigaChatConfig;
import org.articlesblog.dto.GetArticleDTO;
import org.articlesblog.services.ArticleService;
import org.articlesblog.services.GigaChatService;
import org.jsoup.Jsoup;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Tag(name = "GigaChat")
@RestController
@Slf4j
@RequiredArgsConstructor
public class GigaChatController {
    private final GigaChatConfig gigaChatConfig;
    private final GigaChatService gigaChatService;
    private final ArticleService articleService;

    @GetMapping("/summary/{id}")
    @Operation(summary = "Получение пересказа статьи")
    public ResponseEntity<String> generateSummary(@PathVariable Integer id) throws IOException {
        GetArticleDTO article = articleService.getArticle(id);
        String text = Jsoup.parse(article.getText()).text();
        log.info("Текст статьи: " + text);

        String promt = gigaChatConfig.getPromtSummary();
        log.info("Промт: " + promt);

        String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);
        String summary = gigaChatService.getAnswer(encodedText, promt);
        log.info("Суммари" + summary);

        return ResponseEntity.ok(summary);
    }

    @PostMapping("/generate")
    @Operation(summary = "Генерация статьи")
    public ResponseEntity<String> generateArticle(@RequestBody Map<String, String> articleColumns) throws IOException {
        String articleText = articleColumns.get("articleColumns");

        log.info("Получены колонки articleColumns: " + articleText);

        String text = Jsoup.parse(articleText).text();

        String promt = gigaChatConfig.getPromtArticle();
        log.info("Промт: " + promt);

        String generatedArticleText = gigaChatService.getAnswer(text, promt);
        log.info("Сгенерированный текст статьи: " + generatedArticleText);

        return ResponseEntity.ok(generatedArticleText);
    }
}