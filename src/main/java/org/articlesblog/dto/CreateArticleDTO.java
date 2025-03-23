package org.articlesblog.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateArticleDTO {
    private UUID id;
    private String title;
    private String description;
    private String text;
    private String author;
    private String label;
    private MultipartFile multipartFile;
}
