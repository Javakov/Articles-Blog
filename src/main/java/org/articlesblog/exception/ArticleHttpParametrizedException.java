package org.articlesblog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ArticleHttpParametrizedException extends RuntimeException {
    private HttpStatus httpStatus;
    private String error;
    private String errorDescription;
}
