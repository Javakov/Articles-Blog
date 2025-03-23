package org.articlesblog.dto;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditArticleDTO {
    private UUID id;
    private String title;
    private String description;
    private String text;
    private String author;
    private String label;
    private String image;
}
