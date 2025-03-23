package org.articlesblog.exception;

import lombok.extern.slf4j.Slf4j;
import org.articlesblog.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class CommonHandler {

    @ExceptionHandler(ArticleHttpParametrizedException.class)
    ResponseEntity<ErrorDto> handleNotFoundException(ArticleHttpParametrizedException ex) {
        log.error("ReportHttpParametrizedException occurred: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorDto(ex.getError(), ex.getErrorDescription()));
    }
}
