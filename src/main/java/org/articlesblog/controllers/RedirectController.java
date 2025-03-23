package org.articlesblog.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Redirect")
@Slf4j
@RequiredArgsConstructor
@Controller
public class RedirectController {

    @GetMapping("/")
    @Operation(summary = "Переадресация на главную страницу")
    public String slashArticles() {
        return "redirect:/articles/page/1";
    }

    @GetMapping("/articles")
    @Operation(summary = "Переадресация на главную страницу")
    public String anotherSlashArticles() {
        return "redirect:/articles/page/1";
    }
}
