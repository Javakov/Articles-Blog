package org.articlesblog.dto;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetArticleDTO {
    private UUID id;
    private String title;
    private String text;
    private String author;
    private String label;
    private String dateCreate;
    private String dateChange;
    private String image;
}
